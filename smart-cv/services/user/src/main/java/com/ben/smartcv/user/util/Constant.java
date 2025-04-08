package com.ben.smartcv.user.util;

import com.nimbusds.jose.JWSAlgorithm;

public final class Constant {

    public static final JWSAlgorithm ACCESS_TOKEN_SIGNATURE_ALGORITHM = JWSAlgorithm.HS256;

    public static final JWSAlgorithm REFRESH_TOKEN_SIGNATURE_ALGORITHM = JWSAlgorithm.HS384;

}
