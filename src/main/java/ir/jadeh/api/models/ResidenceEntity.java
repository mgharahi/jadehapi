package ir.jadeh.api.models;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "Residences")
public class ResidenceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    private String title;
    private int roomCount;
}