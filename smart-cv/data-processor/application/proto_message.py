import protobuf.cv.command_pb2 as cv_command_pb2
import protobuf.cv.event_pb2 as cv_event_pb2
import protobuf.job.command_pb2 as job_command_pb2
import protobuf.job.event_pb2 as job_event_pb2
from google.protobuf.symbol_database import Default

sym_db = Default()
sym_db.RegisterFileDescriptor(cv_command_pb2.DESCRIPTOR)
sym_db.RegisterFileDescriptor(cv_event_pb2.DESCRIPTOR)
sym_db.RegisterFileDescriptor(job_command_pb2.DESCRIPTOR)
sym_db.RegisterFileDescriptor(job_event_pb2.DESCRIPTOR)

# Lấy message từ protobuf symbol database
ProcessCvCommand = sym_db.GetSymbol("com.ben.smartcv.common.ProcessCvCommand")
CvProcessedEvent = sym_db.GetSymbol("com.ben.smartcv.common.CvProcessedEvent")
JobCreatedEvent = sym_db.GetSymbol("com.ben.smartcv.common.JobCreatedEvent")
ExtractedJobData = sym_db.GetSymbol("com.ben.smartcv.common.ExtractedJobData")