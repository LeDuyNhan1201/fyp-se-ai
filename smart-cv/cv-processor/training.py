import re

import numpy as np
import torch
from datasets import Dataset
from sklearn.metrics import f1_score
from transformers import AutoModelForTokenClassification, TrainingArguments, Trainer, EarlyStoppingCallback, \
    DataCollatorForTokenClassification
from transformers import AutoTokenizer

import csv
import json
import re
from typing import List, Tuple

# def load_skills(file_path="skills.txt") -> List[str]:
#     """Đọc danh sách kỹ năng từ file và chuẩn hóa dữ liệu."""
#     skills = []
#     with open(file_path, "r", encoding="utf-8") as f:
#         for line in f:
#             skill = line.strip()
#             if skill:
#                 skills.append(skill.lower())  # Chuyển về chữ thường
#     return skills

def load_skills(skills: List[str]) -> List[str]:
    """Làm sạch danh sách kỹ năng, bỏ phần mở ngoặc như (Java), (Python)."""
    with open("skills.txt", "r", encoding="utf-8") as f:
        skills = [line.strip() for line in f.readlines()]
        cleaned_skills = []
        for skill in skills:
            clean_skill = re.sub(r'\s*\(.*?\)', '', skill)  # Xóa phần trong ngoặc
            cleaned_skills.append(clean_skill)
    return cleaned_skills

def tokenize_and_label(sentence: str, skills: List[str]) -> List[Tuple[str, str]]:
    """Gán nhãn BIO cho từng token trong câu."""
    words = sentence.split()
    labels = ["O"] * len(words)  # Mặc định là Outside (O)

    for skill in skills:
        pattern = r'\b' + re.escape(skill) + r'\b'  # Tìm từ nguyên vẹn
        for match in re.finditer(pattern, sentence, re.IGNORECASE):
            start, end = match.span()

            # Xác định index trong danh sách từ
            start_idx = len(sentence[:start].split())
            end_idx = len(sentence[:end].split()) - 1

            # Đảm bảo index hợp lệ
            if 0 <= start_idx < len(words):
                labels[start_idx] = "B-SKILL"
            for i in range(start_idx + 1, min(end_idx + 1, len(words))):
                labels[i] = "I-SKILL"

    return list(zip(words, labels))

def process_dataset(dataset_path = "skills_data_set.txt", skills_path = "skills.txt", output_path = "labeled_data.json"):
    """Tiền xử lý dataset và lưu thành file JSON hoặc CSV."""
    skills = load_skills(skills_path)
    labeled_sentences = []

    with open(dataset_path, "r", encoding="utf-8") as f:
        for line in f:
            sentence = line.strip()
            if sentence:
                labeled_tokens = tokenize_and_label(sentence, skills)
                labeled_sentences.append(labeled_tokens)

    # Lưu dữ liệu dưới dạng JSON
    with open(output_path, "w", encoding="utf-8") as json_file:
        json.dump(labeled_sentences, json_file, indent=4, ensure_ascii=False)
    print(f"Labeled data saved to {output_path}")

# Chạy tiền xử lý
process_dataset()

# Load dữ liệu từ file labeled_data.txt
def load_dataset(file_path):
    sentences, labels = [], []
    current_sentence, current_labels = [], []

    with open(file_path, "r", encoding="utf-8") as f:
        for line in f:
            line = line.strip()
            if not line:
                if current_sentence:  # Kết thúc một câu
                    sentences.append(current_sentence)
                    labels.append(current_labels)
                    current_sentence, current_labels = [], []
            else:
                token, tag = line.split()
                current_sentence.append(token)
                current_labels.append(tag)

    return Dataset.from_dict({"tokens": sentences, "ner_tags": labels})

# Load dataset
dataset = load_dataset("labeled_data.txt")

# Kiểm tra dữ liệu
# print(dataset[0])

# Chọn model pre-trained
# model_name = "bert-base-cased"
# tokenizer = AutoTokenizer.from_pretrained(model_name)
#
# # Mapping nhãn thành số
# label_list = ["O", "B-SKILL", "I-SKILL"]
# label2id = { label: i for i, label in enumerate(label_list) }
# id2label = { i: label for label, i in label2id.items() }
#
# # Tokenize dữ liệu
# def tokenize_and_align_labels(examples):
#     tokenized_inputs = tokenizer(examples["tokens"],
#                                  truncation = True,
#                                  is_split_into_words = True,
#                                  padding = "max_length",
#                                  max_length = 128)
#     labels = []
#     for i, label in enumerate(examples["ner_tags"]):
#         word_ids = tokenized_inputs.word_ids(batch_index=i)
#         label_ids = []
#         previous_word_idx = None
#         for word_idx in word_ids:
#             if word_idx is None: label_ids.append(-100)
#
#             elif word_idx != previous_word_idx: label_ids.append(label2id[label[word_idx]])
#
#             else: label_ids.append(label2id[label[word_idx]])
#
#             previous_word_idx = word_idx
#
#         labels.append(label_ids)
#
#     tokenized_inputs["labels"] = labels
#     return tokenized_inputs
#
# # Áp dụng tokenization
# tokenized_dataset = dataset.map(tokenize_and_align_labels, batched = True)
#
# # Load mô hình BERT với số lớp output = số nhãn
# model = AutoModelForTokenClassification.from_pretrained(
#     model_name, num_labels = len(label_list), id2label = id2label, label2id = label2id
# )
#
# def compute_metrics(p):
#     predictions, labels = p
#     predictions = np.argmax(predictions, axis = -1)
#     return {"f1": f1_score(labels, predictions, average = "macro")}
#
# # Định nghĩa tham số huấn luyện
# training_args = TrainingArguments(
#     output_dir = "./skill_ner_model",
#     eval_strategy = "epoch",
#     save_strategy = "epoch",
#     learning_rate = 5e-5,
#     per_device_train_batch_size = 8,
#     per_device_eval_batch_size = 8,
#     num_train_epochs = 5,
#     weight_decay = 0.01,
#     push_to_hub = False,
#
#     # ✅ Bổ sung để EarlyStopping hoạt động
#     # metric_for_best_model = "f1",  # hoặc "loss" tùy thuộc vào bạn muốn theo dõi gì
#     # load_best_model_at_end = True,
#     # greater_is_better = True,  # Nếu theo dõi F1-score thì nên để True, nếu theo dõi loss thì để False
# )
#
# # Tạo trainer
# trainer = Trainer(
#     model= model,
#     args = training_args,
#     train_dataset = tokenized_dataset,
#     eval_dataset = tokenized_dataset,  # Thực tế nên tách tập train & test
#     data_collator = DataCollatorForTokenClassification(tokenizer),
#     # compute_metrics = compute_metrics,  # Đảm bảo có metric để tính toán
#     # callbacks=[EarlyStoppingCallback(early_stopping_patience = 3)]  # Dừng nếu 3 epoch liên tiếp loss không giảm
# )
#
# # Bắt đầu train
# trainer.train()
#
# # Lưu model
# model.save_pretrained("skill_ner_model")
# tokenizer.save_pretrained("skill_ner_model")

