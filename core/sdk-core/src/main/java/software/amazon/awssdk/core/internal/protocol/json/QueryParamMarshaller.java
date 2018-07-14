/*
 * Copyright 2010-2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package software.amazon.awssdk.core.internal.protocol.json;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import software.amazon.awssdk.annotations.SdkInternalApi;
import software.amazon.awssdk.core.protocol.MarshallLocation;

@SdkInternalApi
public final class QueryParamMarshaller {

    public static final JsonMarshaller<String> STRING = new SimpleQueryParamMarshaller<>(
            ValueToStringConverter.FROM_STRING);

    public static final JsonMarshaller<Integer> INTEGER = new SimpleQueryParamMarshaller<>(
            ValueToStringConverter.FROM_INTEGER);

    public static final JsonMarshaller<Long> LONG = new SimpleQueryParamMarshaller<>(ValueToStringConverter.FROM_LONG);

    public static final JsonMarshaller<Double> DOUBLE = new SimpleQueryParamMarshaller<>(
            ValueToStringConverter.FROM_DOUBLE);

    public static final JsonMarshaller<Float> FLOAT = new SimpleQueryParamMarshaller<>(
            ValueToStringConverter.FROM_FLOAT);

    public static final JsonMarshaller<Boolean> BOOLEAN = new SimpleQueryParamMarshaller<>(
            ValueToStringConverter.FROM_BOOLEAN);

    public static final JsonMarshaller<Instant> INSTANT = new SimpleQueryParamMarshaller<>(ValueToStringConverter.FROM_INSTANT);

    public static final JsonMarshaller<List> LIST = (list, context, paramName) -> {
        for (Object listVal : list) {
            context.marshall(MarshallLocation.QUERY_PARAM, listVal, paramName);
        }
    };

    public static final JsonMarshaller<Map> MAP = (val, context, paramName) -> {
        for (Map.Entry<String, ?> mapEntry : ((Map<String, ?>) val).entrySet()) {
            context.marshall(MarshallLocation.QUERY_PARAM, mapEntry.getValue(), mapEntry.getKey());
        }
    };

    private QueryParamMarshaller() {
    }

    private static class SimpleQueryParamMarshaller<T> implements JsonMarshaller<T> {

        private final ValueToStringConverter.ValueToString<T> converter;

        private SimpleQueryParamMarshaller(ValueToStringConverter.ValueToString<T> converter) {
            this.converter = converter;
        }

        @Override
        public void marshall(T val, JsonMarshallerContext context, String paramName) {
            context.request().addParameter(paramName, converter.apply(val));
        }
    }
}