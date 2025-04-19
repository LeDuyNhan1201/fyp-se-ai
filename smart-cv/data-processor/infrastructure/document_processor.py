import os
import pytesseract
from PIL import Image
from pdf2image import convert_from_path


def delete_file(file_path: str):
    """Delete the file if it exists."""
    if os.path.exists(file_path):
        os.remove(file_path)
        print(f"File '{file_path}' deleted successfully")
    else:
        print(f"File '{file_path}' not found")

class DocumentProcessor:
    def __init__(self, lang="eng"):
        self.lang = lang

    def extract_text_from_image(self, image_path: str) -> str:
        """Extract text from an image file using pytesseract."""
        try:
            image = Image.open(image_path)
            text = pytesseract.image_to_string(image, lang=self.lang)
        finally:
            delete_file(image_path)
        return text

    def extract_text_from_pdf(self, pdf_path: str) -> str:
        """Extract text from a PDF file by converting it to images and using pytesseract."""
        try:
            images = convert_from_path(pdf_path)
            extracted_text = "\n".join(pytesseract.image_to_string(img, lang=self.lang) for img in images)
        finally:
            delete_file(pdf_path)
        return extracted_text

