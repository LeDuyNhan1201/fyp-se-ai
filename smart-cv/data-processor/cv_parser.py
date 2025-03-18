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
    pattern = r"\b(?:\+?\d{1,3}[-.\s]?)?(?:\(?\d{2,4}\)?[-.\s]?)?\d{3,4}[-.\s]?\d{3,4}\b"
    match = re.search(pattern, text)
    if match: contact_number = match.group()
    return contact_number

def extract_email(text):
    email = None
    pattern = r"\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}\b"
    match = re.search(pattern, text)
    if match: email = match.group()
    return email

def extract_experience_years(text):
    pattern = r"(\d+)\s*(?:\+|plus|more than|at least)?\s*(?:years|yrs|year)"
    matches = re.findall(pattern, text, re.IGNORECASE)
    return list(set(matches)) if matches else []

def extract_experience_level(text):
    levels = ["Intern", "Junior", "Middle", "Senior", "Lead", "Manager", "Principal", "Director", "Architect"]
    pattern = r"(?i)\b(" + "|".join(levels) + r")\b"
    matches = re.findall(pattern, text)
    return list(set(matches)) if matches else []

def extract_experience(text):
    yoe = extract_experience_years(text)
    level = extract_experience_level(text)
    return yoe + level

def extract_skills(text):
    skills = []
    for skill in read_list_from_txt("extracted_rules/skills.txt"):
        pattern = r"(?<!\w){}(?!\w)".format(re.escape(skill))
        if re.search(pattern, text, re.IGNORECASE):
            skills.append(skill)
    return skills

def extract_education(text):
    education = []
    for keyword in read_list_from_txt("extracted_rules/educations.txt"):
        pattern = r"(?i)\b{}\b".format(re.escape(keyword))
        if re.search(pattern, text):
            education.append(keyword)
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
        "phone": phone,
        "skills": skills,
        "education": education,
    }

def extract_job_info(text):
    phone = extract_contact_number(text)
    email = extract_email(text)
    education = extract_education(text)
    skills = extract_skills(text)
    experience = extract_experience(text)

    return {
        "email": email,
        "phone": phone,
        "skills": skills,
        "education": education,
        "experience": experience
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

job_description = """
Organization Name: NexIoT Solutions
Email: hiring@nexiot.com
Phone: +49 30 1234 5678
Position: Embedded IoT Engineer
Education: Bachelor's or Master's in Embedded Systems, Electrical Engineering, or related fields
Skills: C/C++, RTOS, ARM Cortex, IoT connectivity (Bluetooth, Zigbee, LoRa, NB-IoT), firmware development, power optimization
Languages: English, Spanish (preferred)
Experience: 4+ years in embedded IoT development

Job Description:
We are hiring an Embedded IoT Engineer to design and develop low-power embedded systems for smart devices and industrial IoT applications.

Key Responsibilities:

Develop firmware for embedded IoT devices using C/C++
Optimize power consumption and memory usage in IoT devices
Work with wireless communication protocols (Bluetooth, LoRa, NB-IoT)
Ensure security and reliability of embedded systems
Perform testing and debugging of hardware-software interactions
"""

# cv_data = extract_cv_info(cv_text)
# print(cv_data)
# job_data = extract_job_info(job_description)
# print(job_data)
# score = match_cv_with_jd(cv_data, job_description)
# print(f"Similarity Score: {score:.4f}")

skills_list = [
    'Python', 'Data Analysis', 'Machine Learning', 'Communication', 'Project Management', 'Deep Learning', 'SQL', 'Tableau',
    'Java', 'C++', 'JavaScript', 'HTML', 'CSS', 'React', 'Angular', 'Node.js', 'MongoDB', 'Express.js', 'Git',
    'Research', 'Statistics', 'Quantitative Analysis', 'Qualitative Analysis', 'SPSS', 'R', 'Data Visualization', 'Matplotlib',
    'Seaborn', 'Plotly', 'Pandas', 'Numpy', 'Scikit-learn', 'TensorFlow', 'Keras', 'PyTorch', 'NLTK', 'Text Mining',
    'Natural Language Processing', 'Computer Vision', 'Image Processing', 'OCR', 'Speech Recognition', 'Recommendation Systems',
    'Collaborative Filtering', 'Content-Based Filtering', 'Reinforcement Learning', 'Neural Networks', 'Convolutional Neural Networks',
    'Recurrent Neural Networks', 'Generative Adversarial Networks', 'XGBoost', 'Random Forest', 'Decision Trees', 'Support Vector Machines',
    'Linear Regression', 'Logistic Regression', 'K-Means Clustering', 'Hierarchical Clustering', 'DBSCAN', 'Association Rule Learning',
    'Apache Hadoop', 'Apache Spark', 'MapReduce', 'Hive', 'HBase', 'Apache Kafka', 'Data Warehousing', 'ETL', 'Big Data Analytics',
    'Cloud Computing', 'Amazon Web Services (AWS)', 'Microsoft Azure', 'Google Cloud Platform (GCP)', 'Docker', 'Kubernetes', 'Linux',
    'Shell Scripting', 'Cybersecurity', 'Network Security', 'Penetration Testing', 'Firewalls', 'Encryption', 'Malware Analysis',
    'Digital Forensics', 'CI/CD', 'DevOps', 'Agile Methodology', 'Scrum', 'Kanban', 'Continuous Integration', 'Continuous Deployment',
    'Software Development', 'Web Development', 'Mobile Development', 'Backend Development', 'Frontend Development', 'Full-Stack Development',
    'UI/UX Design', 'Responsive Design', 'Wireframing', 'Prototyping', 'User Testing', 'Adobe Creative Suite', 'Photoshop', 'Illustrator',
    'InDesign', 'Figma', 'Sketch', 'Zeplin', 'InVision', 'Product Management', 'Market Research', 'Customer Development', 'Lean Startup',
    'Business Development', 'Sales', 'Marketing', 'Content Marketing', 'Social Media Marketing', 'Email Marketing', 'SEO', 'SEM', 'PPC',
    'Google Analytics', 'Facebook Ads', 'LinkedIn Ads', 'Lead Generation', 'Customer Relationship Management (CRM)', 'Salesforce',
    'HubSpot', 'Zendesk', 'Intercom', 'Customer Support', 'Technical Support', 'Troubleshooting', 'Ticketing Systems', 'ServiceNow',
    'ITIL', 'Quality Assurance', 'Manual Testing', 'Automated Testing', 'Selenium', 'JUnit', 'Load Testing', 'Performance Testing',
    'Regression Testing', 'Black Box Testing', 'White Box Testing', 'API Testing', 'Mobile Testing', 'Usability Testing', 'Accessibility Testing',
    'Cross-Browser Testing', 'Agile Testing', 'User Acceptance Testing', 'Software Documentation', 'Technical Writing', 'Copywriting',
    'Editing', 'Proofreading', 'Content Management Systems (CMS)', 'WordPress', 'Joomla', 'Drupal', 'Magento', 'Shopify', 'E-commerce',
    'Payment Gateways', 'Inventory Management', 'Supply Chain Management', 'Logistics', 'Procurement', 'ERP Systems', 'SAP', 'Oracle',
    'Microsoft Dynamics', 'Tableau', 'Power BI', 'QlikView', 'Looker', 'Data Warehousing', 'ETL', 'Data Engineering', 'Data Governance',
    'Data Quality', 'Master Data Management', 'Predictive Analytics', 'Prescriptive Analytics', 'Descriptive Analytics', 'Business Intelligence',
    'Dashboarding', 'Reporting', 'Data Mining', 'Web Scraping', 'API Integration', 'RESTful APIs', 'GraphQL', 'SOAP', 'Microservices',
    'Serverless Architecture', 'Lambda Functions', 'Event-Driven Architecture', 'Message Queues', 'GraphQL', 'Socket.io', 'WebSockets'
'Ruby', 'Ruby on Rails', 'PHP', 'Symfony', 'Laravel', 'CakePHP', 'Zend Framework', 'ASP.NET', 'C#', 'VB.NET', 'ASP.NET MVC', 'Entity Framework',
    'Spring', 'Hibernate', 'Struts', 'Kotlin', 'Swift', 'Objective-C', 'iOS Development', 'Android Development', 'Flutter', 'React Native', 'Ionic',
    'Mobile UI/UX Design', 'Material Design', 'SwiftUI', 'RxJava', 'RxSwift', 'Django', 'Flask', 'FastAPI', 'Falcon', 'Tornado', 'WebSockets',
    'GraphQL', 'RESTful Web Services', 'SOAP', 'Microservices Architecture', 'Serverless Computing', 'AWS Lambda', 'Google Cloud Functions',
    'Azure Functions', 'Server Administration', 'System Administration', 'Network Administration', 'Database Administration', 'MySQL', 'PostgreSQL',
    'SQLite', 'Microsoft SQL Server', 'Oracle Database', 'NoSQL', 'MongoDB', 'Cassandra', 'Redis', 'Elasticsearch', 'Firebase', 'Google Analytics',
    'Google Tag Manager', 'Adobe Analytics', 'Marketing Automation', 'Customer Data Platforms', 'Segment', 'Salesforce Marketing Cloud', 'HubSpot CRM',
    'Zapier', 'IFTTT', 'Workflow Automation', 'Robotic Process Automation (RPA)', 'UI Automation', 'Natural Language Generation (NLG)',
    'Virtual Reality (VR)', 'Augmented Reality (AR)', 'Mixed Reality (MR)', 'Unity', 'Unreal Engine', '3D Modeling', 'Animation', 'Motion Graphics',
    'Game Design', 'Game Development', 'Level Design', 'Unity3D', 'Unreal Engine 4', 'Blender', 'Maya', 'Adobe After Effects', 'Adobe Premiere Pro',
    'Final Cut Pro', 'Video Editing', 'Audio Editing', 'Sound Design', 'Music Production', 'Digital Marketing', 'Content Strategy', 'Conversion Rate Optimization (CRO)',
    'A/B Testing', 'Customer Experience (CX)', 'User Experience (UX)', 'User Interface (UI)', 'Persona Development', 'User Journey Mapping', 'Information Architecture (IA)',
    'Wireframing', 'Prototyping', 'Usability Testing', 'Accessibility Compliance', 'Internationalization (I18n)', 'Localization (L10n)', 'Voice User Interface (VUI)',
    'Chatbots', 'Natural Language Understanding (NLU)', 'Speech Synthesis', 'Emotion Detection', 'Sentiment Analysis', 'Image Recognition', 'Object Detection',
    'Facial Recognition', 'Gesture Recognition', 'Document Recognition', 'Fraud Detection', 'Cyber Threat Intelligence', 'Security Information and Event Management (SIEM)',
    'Vulnerability Assessment', 'Incident Response', 'Forensic Analysis', 'Security Operations Center (SOC)', 'Identity and Access Management (IAM)', 'Single Sign-On (SSO)',
    'Multi-Factor Authentication (MFA)', 'Blockchain', 'Cryptocurrency', 'Decentralized Finance (DeFi)', 'Smart Contracts', 'Web3', 'Non-Fungible Tokens (NFTs)']

education_keywords = [
        'Computer Science', 'Information Technology', 'Software Engineering', 'Electrical Engineering', 'Mechanical Engineering', 'Civil Engineering',
        'Chemical Engineering', 'Biomedical Engineering', 'Aerospace Engineering', 'Nuclear Engineering', 'Industrial Engineering', 'Systems Engineering',
        'Environmental Engineering', 'Petroleum Engineering', 'Geological Engineering', 'Marine Engineering', 'Robotics Engineering', 'Biotechnology',
        'Biochemistry', 'Microbiology', 'Genetics', 'Molecular Biology', 'Bioinformatics', 'Neuroscience', 'Biophysics', 'Biostatistics', 'Pharmacology',
        'Physiology', 'Anatomy', 'Pathology', 'Immunology', 'Epidemiology', 'Public Health', 'Health Administration', 'Nursing', 'Medicine', 'Dentistry',
        'Pharmacy', 'Veterinary Medicine', 'Medical Technology', 'Radiography', 'Physical Therapy', 'Occupational Therapy', 'Speech Therapy', 'Nutrition',
        'Sports Science', 'Kinesiology', 'Exercise Physiology', 'Sports Medicine', 'Rehabilitation Science', 'Psychology', 'Counseling', 'Social Work',
        'Sociology', 'Anthropology', 'Criminal Justice', 'Political Science', 'International Relations', 'Economics', 'Finance', 'Accounting', 'Business Administration',
        'Management', 'Marketing', 'Entrepreneurship', 'Hospitality Management', 'Tourism Management', 'Supply Chain Management', 'Logistics Management',
        'Operations Management', 'Human Resource Management', 'Organizational Behavior', 'Project Management', 'Quality Management', 'Risk Management',
        'Strategic Management', 'Public Administration', 'Urban Planning', 'Architecture', 'Interior Design', 'Landscape Architecture', 'Fine Arts',
        'Visual Arts', 'Graphic Design', 'Fashion Design', 'Industrial Design', 'Product Design', 'Animation', 'Film Studies', 'Media Studies',
        'Communication Studies', 'Journalism', 'Broadcasting', 'Creative Writing', 'English Literature', 'Linguistics', 'Translation Studies',
        'Foreign Languages', 'Modern Languages', 'Classical Studies', 'History', 'Archaeology', 'Philosophy', 'Theology', 'Religious Studies',
        'Ethics', 'Education', 'Early Childhood Education', 'Elementary Education', 'Secondary Education', 'Special Education', 'Higher Education',
        'Adult Education', 'Distance Education', 'Online Education', 'Instructional Design', 'Curriculum Development'
        'Library Science', 'Information Science', 'Computer Engineering', 'Software Development', 'Cybersecurity', 'Information Security',
        'Network Engineering', 'Data Science', 'Data Analytics', 'Business Analytics', 'Operations Research', 'Decision Sciences',
        'Human-Computer Interaction', 'User Experience Design', 'User Interface Design', 'Digital Marketing', 'Content Strategy',
        'Brand Management', 'Public Relations', 'Corporate Communications', 'Media Production', 'Digital Media', 'Web Development',
        'Mobile App Development', 'Game Development', 'Virtual Reality', 'Augmented Reality', 'Blockchain Technology', 'Cryptocurrency',
        'Digital Forensics', 'Forensic Science', 'Criminalistics', 'Crime Scene Investigation', 'Emergency Management', 'Fire Science',
        'Environmental Science', 'Climate Science', 'Meteorology', 'Geography', 'Geomatics', 'Remote Sensing', 'Geoinformatics',
        'Cartography', 'GIS (Geographic Information Systems)', 'Environmental Management', 'Sustainability Studies', 'Renewable Energy',
        'Green Technology', 'Ecology', 'Conservation Biology', 'Wildlife Biology', 'Zoology']

# write_list_to_file("extracted_rules/skills.txt", skills_list)
# write_list_to_file("extracted_rules/educations.txt", education_keywords)