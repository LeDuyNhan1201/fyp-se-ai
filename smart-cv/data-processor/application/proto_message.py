from google.protobuf.symbol_database import Default

import protobuf.cv.processor_pb2 as cv_processor_pb2
import protobuf.job.processor_pb2 as job_processor_pb2

sym_db = Default()
sym_db.RegisterFileDescriptor(cv_processor_pb2.DESCRIPTOR)
sym_db.RegisterFileDescriptor(job_processor_pb2.DESCRIPTOR)

# Lấy message từ protobuf symbol database
ExtractedJobData = sym_db.GetSymbol("com.ben.smartcv.common.ExtractedJobData")
ExtractedCvData = sym_db.GetSymbol("com.ben.smartcv.common.ExtractedCvData")