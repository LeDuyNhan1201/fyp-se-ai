# -*- coding: utf-8 -*-
# Generated by the protocol buffer compiler.  DO NOT EDIT!
# NO CHECKED-IN PROTOBUF GENCODE
# source: job.service.proto
# Protobuf Python Version: 5.29.0
"""Generated protocol buffer code."""
from google.protobuf import descriptor as _descriptor
from google.protobuf import descriptor_pool as _descriptor_pool
from google.protobuf import runtime_version as _runtime_version
from google.protobuf import symbol_database as _symbol_database
from google.protobuf.internal import builder as _builder
_runtime_version.ValidateProtobufRuntimeVersion(
    _runtime_version.Domain.PUBLIC,
    5,
    29,
    0,
    '',
    'job.service.proto'
)
# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()


from protobuf.job import command_pb2 as job_dot_command__pb2


DESCRIPTOR = _descriptor_pool.Default().AddSerializedFile(b'\n\x11job.service.proto\x12\x16\x63om.ben.smartcv.common\x1a\x11job.command.proto\"g\n\x10\x45xtractedJobData\x12\r\n\x05\x65mail\x18\x01 \x01(\t\x12\r\n\x05phone\x18\x02 \x01(\t\x12\x11\n\teducation\x18\x03 \x01(\t\x12\x0e\n\x06skills\x18\x04 \x01(\t\x12\x12\n\nexperience\x18\x05 \x01(\t2t\n\x0cJobProcessor\x12\x64\n\x0b\x45xtractData\x12).com.ben.smartcv.common.ProcessJobCommand\x1a(.com.ben.smartcv.common.ExtractedJobData\"\x00\x42/\n\x1a\x63om.ben.smartcv.common.jobB\x0fJobServiceProtoP\x01\x62\x06proto3')

_globals = globals()
_builder.BuildMessageAndEnumDescriptors(DESCRIPTOR, _globals)
_builder.BuildTopDescriptorsAndMessages(DESCRIPTOR, 'job.service_pb2', _globals)
if not _descriptor._USE_C_DESCRIPTORS:
  _globals['DESCRIPTOR']._loaded_options = None
  _globals['DESCRIPTOR']._serialized_options = b'\n\032com.ben.smartcv.common.jobB\017JobServiceProtoP\001'
  _globals['_EXTRACTEDJOBDATA']._serialized_start=64
  _globals['_EXTRACTEDJOBDATA']._serialized_end=167
  _globals['_JOBPROCESSOR']._serialized_start=169
  _globals['_JOBPROCESSOR']._serialized_end=285
# @@protoc_insertion_point(module_scope)
