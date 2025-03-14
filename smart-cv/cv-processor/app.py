from dotenv import load_dotenv
from transformers import pipeline

from infrastructure.dependency_injection import DependencyInjection
from infrastructure.kafka_consumer import KafkaConsumer
import concurrent.futures

def merge_subtokens(ner_results):
    merged_entities = []
    current_entity = None

    for entity in ner_results:
        word = entity['word']
        label = entity['entity']
        score = entity['score']

        # Nếu token bắt đầu bằng '##', ghép vào từ trước đó
        if word.startswith("##"):
            current_entity["word"] += word[2:]  # Bỏ "##" và ghép vào từ trước
            current_entity["score"] = max(current_entity["score"], score)  # Giữ score cao nhất
        else:
            if current_entity:
                merged_entities.append(current_entity)
            current_entity = {"word": word, "entity": label, "score": score}

    if current_entity:
        merged_entities.append(current_entity)

    return merged_entities

def run_consumer(consumer: KafkaConsumer):
    consumer.consume()

def main():
    application_context = DependencyInjection()
    print("App is running")
    application_context.process_job_consumer.consume()
    # consumers = [
    #     application_context.process_job_consumer,
    #     application_context.process_cv_consumer
    # ]
    #
    # with concurrent.futures.ProcessPoolExecutor(max_workers = len(consumers)) as executor:
    #     executor.map(run_consumer, consumers)

    # Load model đã train
#     ner_pipeline = pipeline("ner", model = "skill_ner_model", tokenizer = "skill_ner_model")
#
#     # Câu test
#     sentence = """
#     PHAM CONG THANH
#
# Embedded Software Engineer
#
# EDUCATION
#
# 2021 -
#
# MAJOR: COMPUTER ENGINEERING
#
# SCHOOL: UNIVERSITY OF INFORMATION TECHNOLOGY
#
# GPA: 7.78/10
#
# Q@ 0978526927
# ¥ congthao727@gmail.com
#
# Q HOCHIMINHCITY
#
# PROJECTS
#
# COURSE PROJECT: Wireless Embedded Network
# Systems REMOTE SMART DOOR MONITORING
# SYSTEM
#
# 09/2022 - 12/2022
#
# COURSE PROJECT: THE WEB-BASED
# ATTENDANCE SYSTEM USING RFID
#
# 09/2022 - 12/2022
#
# COURSE PROJECT: COURSE PROJECT: INDOOR
# POSITIONING SYSTEM USING UWB
#
# 02/2023 - 06/2023
#
# COURSE PROJECT: FPGA-Based Face Detection
# System
#
# 02/2022 - 0B/20272
#
# TEAM LEADER
#
# + Analysis and Design
#
# + Build Front-end and Back-end
# + Creating a database
#
# + Connecting hardware modules
# + Bug Fixing
#
# TECHNOLOGY DESCRIPTION
# + Using Arduino Uno R3 as the microcontroller and Wemos as the
#
# server
# + Keypad and RFID
#
# + Build a web system and upload data from MCU to the web and vice
#
# versa
#
# TEAM LEADER
#
# + Analysis and Design
# + Build Back-end
#
# + Connecting hardware modules
# TECHNOLOGY DESCRIPTION
#
# + Using Wemos as a microcontroller
# + Building a web system and uploading data from the MCU to the
# web
#
# TEAM MEMBER
#
# + Find out how to using DWM Firmware and App
# + Programming for DWM1001
#
# TECHNOLOGY DESCRIPTION
#
# + Using DWM1001 Module and Rasperry Pi 3
#
# TEAM LEADER
#
# + System analysis and architectural design
#
# + Program modules using verilog
# TECHILEEY bESCRIPTION
#
# + Run the simulation on Vivado software
#
# COURSE PROJECT: PEOPLE COUNTING SYSTEM
# FOR ENTRY AND EXIT IN A SHOP USING
# RASPBERRY P15
#
# TEAM MEMBER
#
# + System analysis and architectural design
#
# + People counting system training and programming
# 09/2024 - 12/2024 .
# * Quantizing model
#
# TECHNOLOGY DESCRIPTION
#
# * Run model on Raspberry Pi 5
#
# SKILLS
#
# *+ SOFT SKILL: TEAMLEADER, CREATIVE THINKING, CRITICAL THINKING, PRESENTATION, OFFICE, CREATIVE THINKING
#
# * PROGRAMING LANGUAGE: C/C++, PYTHON, HTML/CSS, VERILOG, ASSEMBLY, ...
#
# © topcv.vn
# """
#
#     # Thực hiện NER
#     ner_results = ner_pipeline(sentence)
#
#     # In kết quả
#     for entity in ner_results:
#         print(f"{entity['word']} -> {entity['entity']}, score: {entity['score']:.4f}")

if __name__ == '__main__':
    main()