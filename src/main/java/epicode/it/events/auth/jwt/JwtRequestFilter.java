package epicode.it.events.auth.jwt;

import epicode.it.events.auth.appuser.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component // Indica che questa classe è un componente Spring.
@RequiredArgsConstructor // Genera un costruttore con tutti i campi final automaticamente.
public class JwtRequestFilter extends OncePerRequestFilter {
    private final CustomUserDetailsService customUserDetailsService; // Gestisce il caricamento degli utenti.
    private final JwtTokenUtil jwtTokenUtil; // Gestisce la validazione e l'estrazione di dati dai token JWT.

    public static void extractToken() {
    }

    /**
     * Metodo principale del filtro, eseguito per ogni richiesta.
     * @param request La richiesta HTTP ricevuta.
     * @param response La risposta HTTP da inviare.
     * @param chain La catena di filtri da eseguire.
     * @throws ServletException In caso di errore durante il filtraggio.
     * @throws IOException In caso di errore di input/output.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader("Authorization"); // Recupera l'header "Authorization" dalla richiesta.

        String username = null;
        String jwtToken = null;

        // Controlla se l'header contiene un token JWT valido.
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7); // Rimuove il prefisso "Bearer " per ottenere il token puro.
            try {
                // Estrae l'username dal token JWT.
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                System.out.println("Impossibile ottenere il token JWT");
            } catch (ExpiredJwtException e) {
                System.out.println("Il token JWT è scaduto");
            }
        } else {
            // Se il token non è presente o non è nel formato corretto, continua la catena di filtri.
            chain.doFilter(request, response);
            return;
        }

        // Se l'username è stato estratto e l'utente non è ancora autenticato nel SecurityContext.
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Carica i dettagli dell'utente dal servizio CustomUserDetailsService.
            UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(username);

            // Valida il token JWT con i dettagli dell'utente.
            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                // Crea un oggetto di autenticazione per l'utente.
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                // Imposta i dettagli di autenticazione sulla richiesta corrente.
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // Aggiorna il SecurityContext con l'autenticazione dell'utente.
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        // Continua la catena di filtri.
        chain.doFilter(request, response);
    }

    public static String extractUsername(String token, String secretKey) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes()) // Usa la chiave segreta
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject(); // Generalmente lo username è salvato come "subject"
    }

    public static List<String> extractRoles(String token, String secretKey) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes()) // Usa la chiave segreta
                .build()
                .parseClaimsJws(token)
                .getBody();

        // Supponiamo che i ruoli siano salvati sotto il claim "roles" come lista
        return claims.get("roles", List.class);
    }
}