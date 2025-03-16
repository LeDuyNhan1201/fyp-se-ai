import protobuf.cv.command_pb2 as cv_command_pb2
import protobuf.job.command_pb2 as job_command_pb2
from google.protobuf.symbol_database import Default

sym_db = Default()
sym_db.RegisterFileDescriptor(cv_command_pb2.DESCRIPTOR)
sym_db.RegisterFileDescriptor(job_command_pb2.DESCRIPTOR)

# Lấy message từ protobuf symbol database
PROCESS_CV_COMMAND = sym_db.GetSymbol("com.ben.smartcv.common.ProcessCvCommand")
PROCESS_JOB_COMMAND = sym_db.GetSymbol("com.ben.smartcv.common.ProcessJobCommand")
EXTRACTED_JOB_DATA = sym_db.GetSymbol("com.ben.smartcv.common.ExtractedJobData")