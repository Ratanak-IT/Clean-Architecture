package kh.edu.istad.platform.customer.persistence.document;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("customer_segments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSegmentDocument {
    @Id
    private String id;
    @Indexed(unique = true)
    private String code;
    private String name;
}