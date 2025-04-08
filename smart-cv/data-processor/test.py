import json
import re

import requests

OLLAMA_API_URL = "http://localhost:11434/api/generate"
essay_prompt = """
Organization Name: Intel CorporationEmail: careers@intel.comPhone: +1 (408) 765-8080Position: Embedded Software EngineerEducation: Bachelor's or Master's in Computer Engineering, Electrical Engineering, or related fieldsSkills: C, C++, Embedded Linux, RTOS, Microcontrollers, ARM Cortex, UART, SPI, I2C, Git, YoctoExperience: 3+ years in embedded software development, experience with Linux kernel programmingSalary: $80,000 - $120,000 per year
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
