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
package io.micronaut.serde.support.serdes;

import io.micronaut.serde.config.SerdeConfiguration;
import jakarta.inject.Singleton;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalQueries;
import java.time.temporal.TemporalQuery;

/**
 * LocalTime serde.
 *
 * @since 1.0.0
 */
@Singleton
public final class LocalTimeSerde extends NumericSupportTemporalSerde<LocalTime> {
    /**
     * Allows configuring a default time format for temporal date/time types.
     *
     * @param configuration The configuration
     */
    LocalTimeSerde(SerdeConfiguration configuration) {
        super(configuration, DateTimeFormatter.ISO_LOCAL_TIME, SerdeConfiguration.NumericTimeUnit.NANOSECONDS);
    }

    @Override
    public TemporalQuery<LocalTime> query() {
        return TemporalQueries.localTime();
    }

    @Override
    protected LocalTime fromNanos(long seconds, int nanos) {
        return LocalTime.ofSecondOfDay(seconds).withNano(nanos);
    }

    @Override
    protected long getSecondPart(LocalTime value) {
        return value.toSecondOfDay();
    }

    @Override
    protected int getNanoPart(LocalTime value) {
        return value.getNano();
    }
}
