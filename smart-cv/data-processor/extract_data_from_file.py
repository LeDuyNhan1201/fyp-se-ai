import os
import pytesseract
from PIL import Image
from pdf2image import convert_from_path

def delete_file(file_path: str):
    if os.path.exists(file_path):
        os.remove(file_path)
        print("File deleted successfully")
    else:
        print("File not found")

def extract_text_from_image(image_path: str) -> str:
    image = Image.open(image_path)
    text = pytesseract.image_to_string(image, lang="eng")
    delete_file(image_path)
    return text

def extract_text_from_pdf(pdf_path: str) -> str:
    images = convert_from_path(pdf_path)
    extracted_text = "\n".join(pytesseract.image_to_string(img, lang="eng") for img in images)
    delete_file(pdf_path)
    return extracted_text
