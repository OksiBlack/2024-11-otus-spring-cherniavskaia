package ru.otus.hw.shell;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ShellCommandResultFormatter {
    private final ObjectMapper jacksonObjectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    private ShellCommandResultFormatter() {
    }

    public String format(String prefix, Object... args) {
        StringBuilder result = new StringBuilder(System.lineSeparator());
        result.append(prefix);

        for (Object arg : args) {
            String json = ReflectionToStringBuilder.toString(arg, ToStringStyle.JSON_STYLE);
            try {
                result.append(formatJsonWithIndents(json))
                    .append(System.lineSeparator());
            } catch (JsonProcessingException e) {
                log.warn("Unable to format value: {}", json, e);
            }
        }

        return result.toString();

    }

    public String formatJsonWithIndents(String uglyJsonString) throws JsonProcessingException {
        Object jsonObject = jacksonObjectMapper.readValue(uglyJsonString, Object.class);
        return jacksonObjectMapper.writeValueAsString(jsonObject);
    }
}
