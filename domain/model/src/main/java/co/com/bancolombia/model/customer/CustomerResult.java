package co.com.bancolombia.model.customer;

import co.com.bancolombia.model.Body;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResult {
    private String statusCode;
    private Body body;
}
