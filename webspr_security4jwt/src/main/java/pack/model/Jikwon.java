package pack.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "jikwon")
@Getter
@Setter
public class Jikwon {
    @Id
    private Long jikwonno;

    private String jikwonname;
}
