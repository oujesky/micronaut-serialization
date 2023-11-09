/*
 * Copyright 2017-2023 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.serde.support.deserializers;

import io.micronaut.core.annotation.AnnotationMetadata;
import io.micronaut.serde.support.util.SerdeArgumentConf;

/**
 * Extra deserialization configuration placed at the argument.
 *
 * @author Denis Stepanov
 * @since 2.3.2
 */
public final class DeserializationSerdeArgumentConf extends SerdeArgumentConf {

    public DeserializationSerdeArgumentConf(AnnotationMetadata annotationMetadata) {
        super(annotationMetadata);
    }
}