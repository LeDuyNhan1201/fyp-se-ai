import json
import os
import re

from dotenv import load_dotenv
from google import genai
from google.genai import types

load_dotenv()

job_description = """
Organization Name: Intel CorporationEmail: careers@intel.comPhone: +1 (408) 765-8080Position: Embedded Software EngineerEducation: Bachelor's or Master's in Computer Engineering, Electrical Engineering, or related fieldsSkills: C, C++, Embedded Linux, RTOS, Microcontrollers, ARM Cortex, UART, SPI, I2C, Git, YoctoExperience: 3+ years in embedded software development, experience with Linux kernel programmingSalary: $80,000 - $120,000 per year
"""

resume = """
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

def generate(raw_text, is_job_description = True):
    prompt_resume = f"""
    Extract JSON with only (name[string], email[string], phone[string], educations[string-array], experiences[string-array], skills[string-array]) from: {raw_text}
    """

    prompt_job_description = f"""
    Extract JSON with only (email[string], phone[string], educations[string-array], experiences[string-array], skills[string-array]) from: {raw_text}
    """

    client = genai.Client(
        api_key = os.getenv("GEMINI_API_KEY"),
    )

    model = "gemini-2.0-flash"
    contents = [
        types.Content(
            role = "user",
            parts = [
                types.Part.from_text(text = prompt_job_description if is_job_description else prompt_resume),
            ],
        ),
    ]
    generate_content_config = types.GenerateContentConfig(
        temperature = 1,
        max_output_tokens = 5000,
        response_mime_type = "text/plain",
    )

    result = ""
    for chunk in client.models.generate_content_stream(
        model = model,
        contents = contents,
        config = generate_content_config,
    ):
        result += chunk.text
        # print(chunk.text, end = "")
    print(convert_to_python_dict(result))

if __name__ == "__main__":
    generate(resume, False)
    generate(job_description)


