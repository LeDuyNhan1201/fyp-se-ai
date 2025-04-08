# Generated by the gRPC Python protocol compiler plugin. DO NOT EDIT!
"""Client and server classes corresponding to protobuf-defined services."""
import grpc
import warnings

from protobuf.cv import event_pb2 as cv_dot_event__pb2
from protobuf.cv import service_pb2 as cv_dot_service__pb2

GRPC_GENERATED_VERSION = '1.71.0'
GRPC_VERSION = grpc.__version__
_version_not_supported = False

try:
    from grpc._utilities import first_version_is_lower
    _version_not_supported = first_version_is_lower(GRPC_VERSION, GRPC_GENERATED_VERSION)
except ImportError:
    _version_not_supported = True

if _version_not_supported:
    raise RuntimeError(
        f'The grpc package installed is at version {GRPC_VERSION},'
        + f' but the generated code in cv.service_pb2_grpc.py depends on'
        + f' grpcio>={GRPC_GENERATED_VERSION}.'
        + f' Please upgrade your grpc module to grpcio>={GRPC_GENERATED_VERSION}'
        + f' or downgrade your generated code using grpcio-tools<={GRPC_VERSION}.'
    )


class CvProcessorStub(object):
    """Missing associated documentation comment in .proto file."""

    def __init__(self, channel):
        """Constructor.

        Args:
            channel: A grpc.Channel.
        """
        self.ExtractData = channel.unary_unary(
                '/com.ben.smartcv.common.CvProcessor/ExtractData',
                request_serializer=cv_dot_event__pb2.CvProcessedEvent.SerializeToString,
                response_deserializer=cv_dot_service__pb2.ExtractedCvData.FromString,
                _registered_method=True)


class CvProcessorServicer(object):
    """Missing associated documentation comment in .proto file."""

    def ExtractData(self, request, context):
        """Missing associated documentation comment in .proto file."""
        context.set_code(grpc.StatusCode.UNIMPLEMENTED)
        context.set_details('Method not implemented!')
        raise NotImplementedError('Method not implemented!')


def add_CvProcessorServicer_to_server(servicer, server):
    rpc_method_handlers = {
            'ExtractData': grpc.unary_unary_rpc_method_handler(
                    servicer.ExtractData,
                    request_deserializer=cv_dot_event__pb2.CvProcessedEvent.FromString,
                    response_serializer=cv_dot_service__pb2.ExtractedCvData.SerializeToString,
            ),
    }
    generic_handler = grpc.method_handlers_generic_handler(
            'com.ben.smartcv.common.CvProcessor', rpc_method_handlers)
    server.add_generic_rpc_handlers((generic_handler,))
    server.add_registered_method_handlers('com.ben.smartcv.common.CvProcessor', rpc_method_handlers)


 # This class is part of an EXPERIMENTAL API.
class CvProcessor(object):
    """Missing associated documentation comment in .proto file."""

    @staticmethod
    def ExtractData(request,
            target,
            options=(),
            channel_credentials=None,
            call_credentials=None,
            insecure=False,
            compression=None,
            wait_for_ready=None,
            timeout=None,
            metadata=None):
        return grpc.experimental.unary_unary(
            request,
            target,
            '/com.ben.smartcv.common.CvProcessor/ExtractData',
            cv_dot_event__pb2.CvProcessedEvent.SerializeToString,
            cv_dot_service__pb2.ExtractedCvData.FromString,
            options,
            channel_credentials,
            insecure,
            call_credentials,
            compression,
            wait_for_ready,
            timeout,
            metadata,
            _registered_method=True)
