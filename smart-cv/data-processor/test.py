import requests

OLLAMA_API_URL = "http://localhost:11434/api/generate"
essay_prompt = """
Organization Name: Intel CorporationEmail: careers@intel.comPhone: +1 (408) 765-8080Position: Embedded Software EngineerEducation: Bachelor's or Master's in Computer Engineering, Electrical Engineering, or related fieldsSkills: C, C++, Embedded Linux, RTOS, Microcontrollers, ARM Cortex, UART, SPI, I2C, Git, YoctoExperience: 3+ years in embedded software development, experience with Linux kernel programmingSalary: $80,000 - $120,000 per year
"""

prompt = f"""
Extract JSON with only (email, phone, educations, experiences, skills) from:
"{essay_prompt}"
"""

payload = {
    "model": "deepseek-r1:1.5b",
    "prompt": prompt,
    "stream": False,
}

response = requests.post(OLLAMA_API_URL, json = payload)

if response.status_code == 200:
    result = response.json()
    print("\n📌 IELTS Score Breakdown:")
    print(result["response"])
else:
    print("❌ Lỗi:", response.text)

# import requests
#
# # Địa chỉ API của Ollama
# OLLAMA_API_URL = "http://localhost:11434/api/generate"
# essay_prompt = """
# Write about following topic: Some organisations believe that their employees should dress smartly. Others value quality of work above appearance. Discuss both these views and give your own opinion.
# Write at least 250 words.
# """
# # Bài viết cần chấm điểm
# essay_text = """
# As a matter of fact, with the competitive demands of companies, their labourers strive to achieve their workload effectively by tackling tasks flexibly with their own abilities. Besides that, managers base their evaluations on the final achievements of their workers instead of observing their methods. From my perspective, I suppose this approach has several advantages and disadvantages, which will be mentioned in this essay.
#
# On the one hand, traditional employees frequently obey their managers' schedules and tasks within the established programs. They routinely perform these tasks as part of their mission to complete them as soon as possible, rather than finding optimal ways to save time, labor, and money. Additionally, most workers fear bearing full responsibility whenever they handle their tasks in their own ways, as this may lead to mistakes. Furthermore, many blue-collar workers holistically believe that they should not have to invest significantly more effort than others to receive an equal salary.
#
# On the other hand, there are a massive number of innovative employees who handle their duties intelligently in modern society. Thanks to advancements in technology and science, both blue- and white-collar workers take advantage of these tools to replace outdated and conventional methods in order to achieve higher productivity. Of course, company leaders fully assess the value of their workers through their success, regardless of the methods they use. Last but not least, individuals who frequently tackle tough challenges smartly are more likely to be promoted and can easily secure higher positions and salaries.
#
# In conclusion, workers can achieve significant success and gain more advantages by handling their problems with innovative methods, which are highly valued by their supervisors. This is a fundamental principle for laborers who aspire to stand out based on their ability and quality, rather than merely following traditional methods."""
#
# serious_examiner = """
# You are a highly critical IELTS examiner known for your strict grading. Your job is to evaluate the following essay with a particularly harsh perspective, ensuring that even minor errors are penalized accordingly.
# You are an *IELTS examiner* with a balanced grading style. Your task is to assess the following IELTS Writing Task 2 essay fairly, highlighting strengths and weaknesses but without being overly harsh.
# """
#
# # Tạo prompt yêu cầu AI chấm điểm IELTS
# prompt = f"""
# You are an IELTS examiner. Your task is to evaluate the following IELTS Writing Task 2 essay using the official IELTS band descriptors.
#
# ### *Essay Prompt:*
# "{essay_prompt}"
#
# ---
#
# ### *Essay for Evaluation:*
# "{essay_text}"
#
# ---
#
# ### *Instructions:*
# - *Do NOT provide any extra commentary, thoughts, or reasoning before answering.*
# - *Strictly follow the format below.*
# - *Ensure each section contains at least two strengths, two weaknesses, and two suggestions for improvement.*
# - *DO NOT add unnecessary explanations outside the requested format.*
#
# ###**Scoring Criteria:**
# - *Use numerical scores (1.0 - 9.0) in X.X format, rounded to the nearest 0.5.*
# - *Do NOT include any other scoring scale (e.g., /7.0, /8.0, etc.). Only use /9.0.*
# - *For example points, use 1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0, 5.5, 6.0, 6.5, 7.0, 7.5, 8.0, 8.5, 9.0 etc from 1.0 to 9.0.*
# ---
#
# ### *FORMAT (REQUIRED RESPONSE)*
#
#
# 1.Task Response: X.X
#
#     Strengths:
#     - [What was done well?]
#     - [Another strength]
#
#     Weaknesses:
#     - [Point out areas for improvement]
#     - [Another weakness]
#
#     How to Improve:
#     - [Suggestions to enhance this area]
#     - [Another suggestion]
#
# 2.Coherence & Cohesion: X.X
#
#     Strengths:
#     - [What was done well?]
#     - [Another strength]
#
#     Weaknesses:
#     - [Point out areas for improvement]
#     - [Another weakness]
#
#     How to Improve:
#     - [Suggestions to enhance this area]
#     - [Another suggestion]
#
# 3.Lexical Resource: X.X
#
#     Strengths:
#     - [What was done well?]
#     - [Another strength]
#
#     Weaknesses:
#     - [Point out areas for improvement]
#     - [Another weakness]
#
#     How to Improve:
#     - [Suggestions to enhance this area]
#     - [Another suggestion]
#
# 4.Grammatical Range & Accuracy: X.X
#
#     Strengths:
#     - [What was done well?]
#     - [Another strength]
#
#     Weaknesses:
#     - [Point out areas for improvement]
#     - [Another weakness]
#
#     How to Improve:
#     - [Suggestions to enhance this area]
#     - [Another suggestion]
#
#
# OVERALL BAND SCORE: X.X  (Accurate final score, an average of the four sections)
# ---
# """
#
# # Gửi request đến API Ollama
# payload = {
#     "model": "deepseek-r1:1.5b",  # Model bạn đang chạy
#     "prompt": prompt,
#     "stream": False,
# }
#
# response = requests.post(OLLAMA_API_URL, json=payload)
#
# # Hiển thị kết quả
# if response.status_code == 200:
#     result = response.json()
#     print("\n📌 IELTS Score Breakdown:")
#     print(result["response"])
# else:
#     print("❌ Lỗi:", response.text)