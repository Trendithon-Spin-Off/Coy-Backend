package Trendithon.SpinOff.domain.jobposting.entity;

import Trendithon.SpinOff.domain.jobposting.entity.type.EmploymentType;
import Trendithon.SpinOff.domain.jobposting.entity.type.ExperienceLevel;
import Trendithon.SpinOff.domain.member.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobPosting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String logoUrl;
    private String companyName;
    private Integer viewCount;
    private Integer applicantsCount;
    private String jobTitle;
    private EmploymentType type;
    private LocalDateTime deadLine;
    private ExperienceLevel level;
    @OneToMany
    private List<Member> liker;

    public void increaseViewCount() {
        viewCount++;
    }
}