from transformers import AutoModelForCausalLM, AutoTokenizer, pipeline
import json

# Load model and tokenizer
# model_name_or_path = "TheBloke/CodeLlama-7B-Instruct-GPTQ"
# model = AutoModelForCausalLM.from_pretrained(
#     model_name_or_path,
#     device_map="auto",
#     trust_remote_code=True,
#     revision="main"
# )
#
# tokenizer = AutoTokenizer.from_pretrained(model_name_or_path, use_fast=True)

# Function to extract structured information
def extract_information(text):
    prompt_template = f'''[INST] Extract the following information from the given text and return in JSON format:
- Name
- Email
- Phone
- Skills
- Experiences
- Education
- Major

Input text:
{text}

Output JSON:
[/INST]'''

    # Create pipeline
    generator = pipeline(
        "text-generation",
        model="facebook/opt-1.3b",
        device=-1,  # Chạy trên CPU (-1)
        max_new_tokens=200,  # Giới hạn số token được sinh ra (khuyến nghị)
        do_sample=True,  # Cho phép sampling để kết quả đa dạng hơn
        temperature=0.7,  # Điều chỉnh mức độ sáng tạo (thấp hơn -> ít sáng tạo)
        top_k=50,  # Chọn các token có xác suất cao nhất
        top_p=0.9  # Áp dụng nucleus sampling
    )
    result = generator(prompt_template, max_new_tokens=200)
    print(result)
    # Extract JSON from the result
    try:
        extracted_data = json.loads(result[0]["generated_text"])
        return extracted_data
    except json.JSONDecodeError:
        return {"error": "Invalid JSON output"}

# Example usage
text_input = """
PHAM CONG THANH

Embedded Software Engineer

EDUCATION

2021 -

MAJOR: COMPUTER ENGINEERING

SCHOOL: UNIVERSITY OF INFORMATION TECHNOLOGY

GPA: 7.78/10

Q@ 0978526927
¥ congthao727@gmail.com

Q HOCHIMINHCITY

PROJECTS

COURSE PROJECT: Wireless Embedded Network
Systems REMOTE SMART DOOR MONITORING
SYSTEM

09/2022 - 12/2022

COURSE PROJECT: THE WEB-BASED
ATTENDANCE SYSTEM USING RFID

09/2022 - 12/2022

COURSE PROJECT: COURSE PROJECT: INDOOR
POSITIONING SYSTEM USING UWB

02/2023 - 06/2023

COURSE PROJECT: FPGA-Based Face Detection
System

02/2022 - 0B/20272

TEAM LEADER

+ Analysis and Design

+ Build Front-end and Back-end
+ Creating a database

+ Connecting hardware modules
+ Bug Fixing

TECHNOLOGY DESCRIPTION
+ Using Arduino Uno R3 as the microcontroller and Wemos as the

server
+ Keypad and RFID

+ Build a web system and upload data from MCU to the web and vice

versa

TEAM LEADER

+ Analysis and Design
+ Build Back-end

+ Connecting hardware modules
TECHNOLOGY DESCRIPTION

+ Using Wemos as a microcontroller
+ Building a web system and uploading data from the MCU to the
web

TEAM MEMBER

+ Find out how to using DWM Firmware and App
+ Programming for DWM1001

TECHNOLOGY DESCRIPTION

+ Using DWM1001 Module and Rasperry Pi 3

TEAM LEADER

+ System analysis and architectural design

+ Program modules using verilog
TECHILEEY bESCRIPTION

+ Run the simulation on Vivado software

COURSE PROJECT: PEOPLE COUNTING SYSTEM
FOR ENTRY AND EXIT IN A SHOP USING
RASPBERRY P15

TEAM MEMBER

+ System analysis and architectural design

+ People counting system training and programming
09/2024 - 12/2024 .
* Quantizing model

TECHNOLOGY DESCRIPTION

* Run model on Raspberry Pi 5

SKILLS

*+ SOFT SKILL: TEAMLEADER, CREATIVE THINKING, CRITICAL THINKING, PRESENTATION, OFFICE, CREATIVE THINKING

* PROGRAMING LANGUAGE: C/C++, PYTHON, HTML/CSS, VERILOG, ASSEMBLY, ...

© topcv.vn
"""
extracted_info = extract_information(text_input)

# Print extracted JSON
print(json.dumps(extracted_info, indent=4))
