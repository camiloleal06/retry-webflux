package co.com.bancolombia.api;

import co.com.bancolombia.model.Body;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FrontResponse {
    private String statusCode;
    private Body body;
}
