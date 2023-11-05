/*
 * Copyright 2017-2021 original authors
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
package io.micronaut.serde.support.util;

import io.micronaut.core.annotation.AnnotationMetadata;
import io.micronaut.core.annotation.Internal;
import io.micronaut.core.reflect.ReflectionUtils;
import io.micronaut.core.type.Argument;
import io.micronaut.core.util.ArrayUtils;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.serde.config.annotation.SerdeConfig;

import java.util.Objects;

/**
 * Utility methods for annotations.
 *
 * @since 1.0.0
 */
@Internal
public final class SerdeAnnotationUtil {
    /**
     * Resolve json views.
     * @param beanMetadata The bean metadata
     * @param annotationMetadata The element metadata
     * @return The views
     */
    public static Class<?>[] resolveViews(AnnotationMetadata beanMetadata,
                                          AnnotationMetadata annotationMetadata) {
        Class<?>[] views = annotationMetadata.classValues(SerdeConfig.class, SerdeConfig.VIEWS);
        if (ArrayUtils.isEmpty(views)) {
            views = beanMetadata.classValues(SerdeConfig.class, SerdeConfig.VIEWS);
        }
        views = ArrayUtils.isNotEmpty(views) ? CollectionUtils.setOf(views).toArray(ReflectionUtils.EMPTY_CLASS_ARRAY) : null;
        return views;
    }

    /**
     * Resolve argument for property to de-/serialize to.
     *
     * @param argument The current property argument
     * @param annotationMetadata The property annotation metadata
     * @param configMember Name of the member of {@link SerdeConfig} to consider - eithe {@link SerdeConfig#DESERIALIZE_AS} or {@link SerdeConfig#SERIALIZE_AS}
     * @return Type defined in {@link SerdeConfig} annotation takes precedence, if not defined then original argument is used
     * @param <T> Type of the argument
     */
    public static <T> Argument<T> resolveArgument(Argument<T> argument, AnnotationMetadata annotationMetadata, String configMember) {
        if (Objects.equals(configMember, SerdeConfig.DESERIALIZE_AS) || Objects.equals(configMember, SerdeConfig.SERIALIZE_AS)) {
            @SuppressWarnings("unchecked") Class<T> classFromConfig = (Class<T>) annotationMetadata.classValue(SerdeConfig.class, configMember).orElse(null);
            if (classFromConfig != null) {
                return Argument.of(classFromConfig, argument.getName(), argument.getAnnotationMetadata(), argument.getTypeParameters());
            }
            return argument;
        } else {
            throw new IllegalArgumentException("Expected either " + SerdeConfig.DESERIALIZE_AS + "' or '" + SerdeConfig.SERIALIZE_AS + "' to consider for argument resolution");
        }
    }
}
