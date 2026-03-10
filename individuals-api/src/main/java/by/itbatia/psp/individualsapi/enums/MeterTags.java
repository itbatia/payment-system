package by.itbatia.psp.individualsapi.enums;

import java.util.List;

import io.micrometer.core.instrument.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Batsian_SV
 */
@Getter
@RequiredArgsConstructor
public enum MeterTags {

    SUCCESS(List.of(Tag.of("status", "success"))),
    FAIL(List.of(Tag.of("status", "fail")));

    private final List<Tag> tags;
}
