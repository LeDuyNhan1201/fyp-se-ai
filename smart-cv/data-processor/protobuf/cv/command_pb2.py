# -*- coding: utf-8 -*-
# Generated by the protocol buffer compiler.  DO NOT EDIT!
# NO CHECKED-IN PROTOBUF GENCODE
# source: cv.command.proto
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
    'cv.command.proto'
)
# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()




DESCRIPTOR = _descriptor_pool.Default().AddSerializedFile(b'\n\x10\x63v.command.proto\x12\x16\x63om.ben.smartcv.common\"5\n\x10ProcessCvCommand\x12\r\n\x05\x63v_id\x18\x01 \x01(\t\x12\x12\n\nobject_key\x18\x02 \x01(\t\"=\n\x18RollbackProcessCvCommand\x12\r\n\x05\x63v_id\x18\x01 \x01(\t\x12\x12\n\nobject_key\x18\x02 \x01(\tB-\n\x19\x63om.ben.smartcv.common.cvB\x0e\x43vCommandProtoP\x01\x62\x06proto3')

_globals = globals()
_builder.BuildMessageAndEnumDescriptors(DESCRIPTOR, _globals)
_builder.BuildTopDescriptorsAndMessages(DESCRIPTOR, 'cv.command_pb2', _globals)
if not _descriptor._USE_C_DESCRIPTORS:
  _globals['DESCRIPTOR']._loaded_options = None
  _globals['DESCRIPTOR']._serialized_options = b'\n\031com.ben.smartcv.common.cvB\016CvCommandProtoP\001'
  _globals['_PROCESSCVCOMMAND']._serialized_start=44
  _globals['_PROCESSCVCOMMAND']._serialized_end=97
  _globals['_ROLLBACKPROCESSCVCOMMAND']._serialized_start=99
  _globals['_ROLLBACKPROCESSCVCOMMAND']._serialized_end=160
# @@protoc_insertion_point(module_scope)
