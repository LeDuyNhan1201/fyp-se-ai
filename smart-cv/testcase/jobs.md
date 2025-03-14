```` shell
-H "Authorization: Bearer <token>" \

curl -X POST "http://localhost:30003/job" \
     -H "Content-Type: application/json" \
     -d '{
       "organizationName": "SmartTech Solutions",
       "position": "IoT Software Engineer",
       "requirements": "Organization Name: SmartTech Solutions\\nEmail: careers@smarttech.com\\nPhone: +1 123-456-7890\\nPosition: IoT Software Engineer\\nEducation: Bachelor's or Master's in Computer Science, Electrical Engineering, or related field.\\nSkills: C, C++, Python, MQTT, RESTful APIs, Embedded Linux, RTOS, IoT security, cloud platforms (AWS IoT, Azure IoT).\\nLanguages: English (Required), German (Preferred).\\nExperience: 3+ years in IoT software development, experience with microcontrollers and wireless communication (BLE, Zigbee, LoRa, Wi-Fi)."
     }'
     
curl -X POST "http://localhost:30003/job" \
     -H "Content-Type: application/json" \
     -d '{
       "organizationName": "NexGen Embedded",
       "position": "Embedded Systems Engineer",
       "requirements": "Organization Name: NexGen Embedded\\nEmail: hr@nexgen.com\\nPhone: +44 789-123-4567\\nPosition: Embedded Systems Engineer\\nEducation: Bachelor's or Master's in Embedded Systems, Electronics, or related field.\\nSkills: C, C++, RTOS, ARM Cortex, SPI, I2C, UART, PCB design, signal processing, low-power optimization.\\nLanguages: English (Required), French (Preferred).\\nExperience: 5+ years of experience in designing and implementing embedded systems for industrial automation."
     }'

curl -X POST "http://localhost:30003/job" \
     -H "Content-Type: application/json" \
     -d '{
       "organizationName": "CloudIoT Inc.",
       "position": "IoT Cloud Engineer",
       "requirements": "Organization Name: CloudIoT Inc.\\nEmail: jobs@cloudiot.com\\nPhone: +1 987-654-3210\\nPosition: IoT Cloud Engineer\\nEducation: Bachelor's in Computer Science or related field.\\nSkills: AWS IoT, Azure IoT Hub, Google Cloud IoT, MQTT, Kafka, Kubernetes, Python, Go, Terraform, cloud security.\\nLanguages: English (Required), Spanish (Preferred).\\nExperience: 4+ years of experience in cloud-based IoT application development."
     }'

curl -X POST "http://localhost:30003/job" \
     -H "Content-Type: application/json" \
     -d '{
       "organizationName": "TechSolutions Ltd.",
       "position": "Firmware Engineer",
       "requirements": "Organization Name: TechSolutions Ltd.\\nEmail: careers@techsolutions.com\\nPhone: +33 456-789-0123\\nPosition: Firmware Engineer\\nEducation: Bachelor's or Master's in Computer Engineering, Embedded Systems, or related field.\\nSkills: Embedded C, C++, FreeRTOS, Microcontrollers (STM32, ESP32, Nordic), JTAG debugging, power management, wireless protocols
     }'
     
curl -X POST "http://localhost:30003/job" \
     -H "Content-Type: application/json" \
     -d '{
       "organizationName": "SecureIoT Solutions",
       "position": "IoT Security Engineer",
       "requirements": "Organization Name: SecureIoT Solutions\\nEmail: security@secureiot.com\\nPhone: +1 800-123-4567\\nPosition: IoT Security Engineer\\nEducation: Master's in Cybersecurity, Computer Science, or a related field.\\nSkills: IoT security protocols, cryptographic algorithms, penetration testing, TLS, SSH, network security, firmware security.\\nLanguages: English (Required), Chinese (Preferred).\\nExperience: 5+ years of experience in IoT security architecture and secure firmware development."
     }'

curl -X POST "http://localhost:30003/job" \
     -H "Content-Type: application/json" \
     -d '{
       "organizationName": "AIoT Labs",
       "position": "Embedded AI Engineer",
       "requirements": "Organization Name: AIoT Labs\\nEmail: hiring@aiotlabs.com\\nPhone: +91 999-888-7777\\nPosition: Embedded AI Engineer\\nEducation: PhD or Master’s in AI, Embedded Systems, or Computer Vision.\\nSkills: Edge AI, TinyML, TensorFlow Lite, OpenCV, DSP, FPGA programming, Python, C++.\\nLanguages: English (Required), Japanese (Preferred).\\nExperience: 4+ years of experience in deploying AI models on embedded hardware."
     }'

curl -X POST "http://localhost:30003/job" \
     -H "Content-Type: application/json" \
     -d '{
       "organizationName": "InnovateIoT",
       "position": "IoT Hardware Engineer",
       "requirements": "Organization Name: InnovateIoT\\nEmail: careers@innovateiot.com\\nPhone: +49 654-321-0987\\nPosition: IoT Hardware Engineer\\nEducation: Bachelor's or Master’s in Electrical Engineering, Mechatronics, or Embedded Systems.\\nSkills: PCB design, RF circuits, signal integrity analysis, BLE, Zigbee, LoRaWAN, SPI, I2C, UART.\\nLanguages: English (Required), German (Preferred).\\nExperience: 6+ years of experience designing IoT hardware solutions."
     }'

curl -X POST "http://localhost:30003/job" \
     -H "Content-Type: application/json" \
     -d '{
       "organizationName": "SmartFactory Tech",
       "position": "Industrial IoT Engineer",
       "requirements": "Organization Name: SmartFactory Tech\\nEmail: hr@smartfactory.com\\nPhone: +1 234-567-8901\\nPosition: Industrial IoT Engineer\\nEducation: Bachelor's in Industrial Engineering, IoT, or Automation.\\nSkills: PLC programming, SCADA, Modbus, OPC-UA, MQTT, edge computing, sensor integration.\\nLanguages: English (Required), Italian (Preferred).\\nExperience: 5+ years in industrial IoT system development."
     }'

curl -X POST "http://localhost:30003/job" \
     -H "Content-Type: application/json" \
     -d '{
       "organizationName": "AutoEmbedded Tech",
       "position": "Automotive Embedded Software Engineer",
       "requirements": "Organization Name: AutoEmbedded Tech\\nEmail: jobs@autoembedded.com\\nPhone: +81 345-678-9012\\nPosition: Automotive Embedded Software Engineer\\nEducation: Bachelor's or Master's in Automotive Engineering, Embedded Systems, or related field.\\nSkills: AUTOSAR, CAN, LIN, ISO 26262, MATLAB/Simulink, C, C++, functional safety.\\nLanguages: English (Required), Japanese (Preferred).\\nExperience: 4+ years in embedded software development for automotive applications."
     }'

curl -X POST "http://localhost:30003/job" \
     -H "Content-Type: application/json" \
     -d '{
       "organizationName": "HomeAutomation Inc.",
       "position": "Smart Home IoT Engineer",
       "requirements": "Organization Name: HomeAutomation Inc.\\nEmail: careers@homeautomation.com\\nPhone: +1 654-987-3210\\nPosition: Smart Home IoT Engineer\\nEducation: Bachelor's in Electrical Engineering, Computer Science, or IoT-related field.\\nSkills: Home automation protocols (Z-Wave, Zigbee, Matter), smart sensors, cloud-based home automation, voice assistants integration.\\nLanguages: English (Required), Spanish (Preferred).\\nExperience: 3+ years developing and deploying smart home IoT solutions."
     }'

````