import json
import re

import requests

OLLAMA_API_URL = "http://localhost:11434/api/generate"
essay_prompt = """
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

Analysis and Design


Build Front-end and Back-end
Creating a database


Connecting hardware modules
Bug Fixing


TECHNOLOGY DESCRIPTION
Using Arduino Uno R3 as the microcontroller and Wemos as the


server
Keypad and RFID


Build a web system and upload data from MCU to the web and vice


versa

TEAM LEADER

Analysis and Design
Build Back-end


Connecting hardware modules

TECHNOLOGY DESCRIPTION

Using Wemos as a microcontroller
Building a web system and uploading data from the MCU to the

web

TEAM MEMBER

Find out how to using DWM Firmware and App
Programming for DWM1001


TECHNOLOGY DESCRIPTION

Using DWM1001 Module and Rasperry Pi 3


TEAM LEADER

System analysis and architectural design


Program modules using verilog

TECHILEEY bESCRIPTION

Run the simulation on Vivado software


COURSE PROJECT: PEOPLE COUNTING SYSTEM
FOR ENTRY AND EXIT IN A SHOP USING
RASPBERRY P15

TEAM MEMBER

System analysis and architectural design


People counting system training and programming

09/2024 - 12/2024 .
Quantizing model


TECHNOLOGY DESCRIPTION

Run model on Raspberry Pi 5


SKILLS

*+ SOFT SKILL: TEAMLEADER, CREATIVE THINKING, CRITICAL THINKING, PRESENTATION, OFFICE, CREATIVE THINKING

PROGRAMING LANGUAGE: C/C++, PYTHON, HTML/CSS, VERILOG, ASSEMBLY, ...


© topcv.vn
"""

prompt = f"""
You will receive a text input.  
Extract a valid JSON object with the following fields only:  
- email (string)  
- phone (string)  
- educations (array of strings)  
- experiences (array of strings)  
- skills (array of strings)  

Rules:  
- Do not explain anything.  
- Do not include markdown formatting.  
- Only respond with a JSON object wrapped in this exact block:  
```json
<response>
```
- If a field is not found, return it as an empty string or an empty array.
- Do not include any commentary, intro, or conclusion.
Input text: {essay_prompt}
"""

payload = {
    "model": "deepseek-r1:1.5b",
    "prompt": prompt,
    "stream": False,
}

response = requests.post(OLLAMA_API_URL, json = payload)

def convert_to_python_dict(text):
    match = re.search(r"```json(.*?)```", text, re.DOTALL)
    if match:
        json_str = match.group(1).strip()
        try:
            return json.loads(json_str)
        except json.JSONDecodeError as e:
            print(f"JSONDecodeError: {e}")
            return None
    else:
        return None

if response.status_code == 200:
    result = response.json()
    raw = result["response"]
    print(raw)
    print(convert_to_python_dict(raw))
else:
    print("❌ Lỗi:", response.text)