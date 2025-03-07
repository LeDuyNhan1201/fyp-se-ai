import re
from sentence_transformers import SentenceTransformer
from sklearn.metrics.pairwise import cosine_similarity


def extract_name(text):
    name = None
    pattern = r"\b[A-Z][a-z]+(?:\s[A-Z][a-z]+)*\b"
    match = re.search(pattern, text)
    if match: name = match.group()
    return name

def extract_contact_number(text):
    contact_number = None
    pattern = r"\b(?:\+?\d{1,3}[-.\s]?)?\(?\d{3}\)?[-.\s]?\d{3}[-.\s]?\d{4}\b"
    match = re.search(pattern, text)
    if match: contact_number = match.group()
    return contact_number

def extract_email(text):
    email = None
    pattern = r"\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}\b"
    match = re.search(pattern, text)
    if match: email = match.group()
    return email

def extract_skills(text):
    skills = []
    for skill in read_list_from_txt("skills.txt"):
        pattern = r"(?<!\w){}(?!\w)".format(re.escape(skill))
        match = re.search(pattern, text, re.IGNORECASE)
        if match: skills.append(skill)
    return skills

def extract_education(text):
    education = []
    for keyword in read_list_from_txt("educations.txt"):
        pattern = r"(?i)\b{}\b".format(re.escape(keyword))
        match = re.search(pattern, text)
        if match: education.append(match.group())
    return education

def extract_cv_info(text):
    name = extract_name(text)
    phone = extract_contact_number(text)
    email = extract_email(text)
    education = extract_education(text)
    skills = extract_skills(text)

    return {
        "name": name,
        "email": email,
        "phone_number": phone,
        "skills": skills,
        "education": education,
    }

# Load SBERT model
model = SentenceTransformer("all-MiniLM-L6-v2")

def match_cv_with_jd(cv_data, job_description):
    cv_skills = ", ".join(cv_data.get("skills", []))
    cv_education = ", ".join(cv_data.get("education", []))
    cv_str= f"Skills: {cv_skills}. Education: {cv_education}."

    # Encode CV và JD thành vector
    cv_vector = model.encode([cv_str])[0]
    jd_vector = model.encode([job_description])[0]

    # Tính toán độ tương đồng cosine
    similarity_score = cosine_similarity([cv_vector], [jd_vector])[0][0]
    return similarity_score

def write_list_to_file(file_path, data_list):
    with open(file_path, "w", encoding="utf-8") as f:
        f.write("\n".join(map(str, data_list)))

def read_list_from_txt(file_path):
    with open(file_path, "r", encoding="utf-8") as f:
        return f.read().splitlines()

cv_text = """
Afreen Jamadar\nActive member of IIIT Committee in Third year\n\nSangli, Maharashtra - Email me on Indeed: indeed.com/r/Afreen-Jamadar/8baf379b705e37c6\n\nI wish to use my knowledge, skills and conceptual understanding to create excellent team\nenvironments and work consistently achieving organization objectives believes in taking initiative\nand work to excellence in my work.\n\nWORK EXPERIENCE\n\nActive member of IIIT Committee in Third year\n\nCisco Networking -  Kanpur, Uttar Pradesh\n\norganized by Techkriti IIT Kanpur and Azure Skynet.\nPERSONALLITY TRAITS:\n• Quick learning ability\n• hard working\n\nEDUCATION\n\nPG-DAC\n\nCDAC ACTS\n\n2017\n\nBachelor of Engg in Information Technology\n\nShivaji University Kolhapur -  Kolhapur, Maharashtra\n\n2016\n\nSKILLS\n\nDatabase (Less than 1 year), HTML (Less than 1 year), Linux. (Less than 1 year), MICROSOFT\nACCESS (Less than 1 year), MICROSOFT WINDOWS (Less than 1 year)\n\nADDITIONAL INFORMATION\n\nTECHNICAL SKILLS:\n\n• Programming Languages: C, C++, Java, .net, php.\n• Web Designing: HTML, XML\n• Operating Systems: Windows […] Windows Server 2003, Linux.\n• Database: MS Access, MS SQL Server 2008, Oracle 10g, MySql.\n\nhttps://www.indeed.com/r/Afreen-Jamadar/8baf379b705e37c6?isid=rex-download&ikw=download-top&co=IN
"""

job_description = """
Job Type: Full-time
Experience Level: Mid-Senior

Job Description:
We are seeking an IoT Embedded Systems Engineer to design, develop, and optimize embedded systems for IoT applications. The ideal candidate will have experience in embedded programming, sensor integration, and wireless communication protocols.

Responsibilities:
Develop firmware for IoT devices using C/C++ or Python.
Work with microcontrollers (ESP32, STM32, ARM Cortex).
Integrate sensors and peripherals via SPI, I2C, UART, and GPIO.
Optimize power consumption and system performance.
Ensure IoT security and data encryption.
Collaborate with cloud developers to integrate IoT devices with platforms like AWS IoT, Azure IoT, or Google Cloud IoT.
Requirements:
Bachelor's/Master’s degree in Computer Science, Electrical Engineering, or related field.
Strong experience with embedded C/C++ and RTOS.
Familiarity with wireless communication protocols (BLE, LoRa, Zigbee, MQTT).
Hands-on experience with IoT platforms and device-to-cloud communication.
Knowledge of PCB design and hardware debugging is a plus.
"""

# cv_data = extract_cv_info(cv_text)
# print(cv_data)
# score = match_cv_with_jd(cv_data, job_description)
# print(f"Similarity Score: {score:.4f}")

# write_list_to_file("educations.txt", education_keywords)