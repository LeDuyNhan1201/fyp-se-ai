import os
import re
import json
from typing import Optional, Dict, Any

from dotenv import load_dotenv
from google.genai import Client, types

load_dotenv()

def _build_prompt(text: str, is_job_description: bool) -> str:
    if is_job_description:
        return f"""
        Extract JSON with only (email[string], phone[string], educations[string-array], experiences[string-array], skills[string-array]) from: {text}
        if content of input is wrong format for Job description or empty return null, mustn't generate any other random json
        """
    return f"""
    Extract JSON with only (name[string], email[string], phone[string], educations[string-array], experiences[string-array], skills[string-array]) from: {text}
    if content of input is wrong format for Resume or empty return null, mustn't generate any other random json
    """


def _convert_to_python_dict(text: str) -> Optional[Dict[str, Any]]:
    match = re.search(r"```json(.*?)```", text, re.DOTALL)
    if match:
        json_str = match.group(1).strip()
        try:
            return json.loads(json_str)
        except json.JSONDecodeError as e:
            print(f"JSONDecodeError: {e}")
    return None


class DataParser:
    def __init__(self, api_key: Optional[str] = None, model: str = "gemini-2.0-flash"):
        self.api_key = api_key or os.getenv("GEMINI_API_KEY")
        self.client = Client(api_key=self.api_key)
        self.model = model

    def parse(self, raw_text: str, is_job_description: bool = True) -> Optional[Dict[str, Any]]:
        prompt = _build_prompt(raw_text, is_job_description)
        contents = [
            types.Content(
                role="user",
                parts=[types.Part.from_text(text=prompt)],
            )
        ]

        config = types.GenerateContentConfig(
            temperature=1,
            max_output_tokens=5000,
            response_mime_type="text/plain",
        )

        result = ""
        for chunk in self.client.models.generate_content_stream(
            model=self.model,
            contents=contents,
            config=config,
        ):
            result += chunk.text

        return _convert_to_python_dict(result)


# if __name__ == "__main__":
#     resume = """Paste your resume text here"""
#     job_description = """Paste job description here"""
#
#     parser = ResumeJobParser()
#     resume_data = parser.parse(resume, is_job_description=False)
#     job_data = parser.parse(job_description)
#
#     print("Parsed Resume:", resume_data)
#     print("Parsed Job Description:", job_data)
