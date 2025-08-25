package nl.ckarakoc.jellycash.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "refresh_token_id", nullable = false)
	private Long refreshTokenId;

	@Column(nullable = false, unique = true)
	private String token;

	@Column(nullable = false)
	private Date expiryDate;

	//private boolean revoked = false;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", referencedColumnName = "user_id")
	@ToString.Exclude
	private User user;
}