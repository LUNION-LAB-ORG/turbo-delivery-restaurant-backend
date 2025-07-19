package com.lunionlab.turbo_restaurant.config;

import com.lunionlab.turbo_restaurant.exception.ErreurException;
import com.lunionlab.turbo_restaurant.exception.ObjetNonAuthoriseException;
import com.lunionlab.turbo_restaurant.exception.ObjetNonTrouveException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestControllerAdvice
public class TransformationException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e
    ) {
        List<String> erreurs = e.getBindingResult().getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("erreurs", erreurs);
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ProblemDetail handleBadCredentialsException(BadCredentialsException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(ObjetNonTrouveException.class)
    public ProblemDetail handleObjetNonTrouver(ObjetNonTrouveException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleException(Exception e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(ObjetNonAuthoriseException.class)
    public ProblemDetail handleObjetNonAuthoriseException(ObjetNonAuthoriseException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler(ErreurException.class)
    public ProblemDetail handleErreurException(ErreurException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    /*
    @ExceptionHandler(DecisionException.class)
    public ProblemDetail handleDecisionException(DecisionException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(ClientException.class)
    public ProblemDetail handleClientException(ClientException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(BaremeNotationException.class)
    public ProblemDetail handleBaremeNotationException(BaremeNotationException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(NonTrouveException.class)
    public ProblemDetail handleNonTrouveException(NonTrouveException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(ClientParticulierException.class)
    public ProblemDetail handleClientParticulierException(ClientParticulierException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(DomaineActiviteException.class)
    public ProblemDetail handleDomaineActiviteException(DomaineActiviteException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(ProfileException.class)
    public ProblemDetail handleProfileException(ProfileException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(PermissionException.class)
    public ProblemDetail handlePermissionException(PermissionException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(MotifEnumereException.class)
    public ProblemDetail handleMotifEnumereException(MotifEnumereException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(ListeControleException.class)
    public ProblemDetail handleListeControleException(ListeControleException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(NiveauRisqueException.class)
    public ProblemDetail handleNiveauRisqueException(NiveauRisqueException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(OrganeValidationException.class)
    public ProblemDetail handleOrganeValidationException(OrganeValidationException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(PaysSousSanctionException.class)
    public ProblemDetail handlePaysSousSanctionException(PaysSousSanctionException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(EntrepriseException.class)
    public ProblemDetail handleEntrepriseException(EntrepriseException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(ValidateurException.class)
    public ProblemDetail handleValidateurException(ValidateurException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(SuppressionImpossibleException.class)
    public ProblemDetail handleSuppressionImpossibleException(SuppressionImpossibleException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(AbonnementException.class)
    public ProblemDetail handleAbonnementException(AbonnementException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(EmailException.class)
    public ProblemDetail handleEmailException(EmailException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(CircuitValidationException.class)
    public ProblemDetail handleCircuitValidationException(CircuitValidationException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(PisteAuditException.class)
    public ProblemDetail handlePisteAuditException(PisteAuditException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(BaremeOperationnelNotationException.class)
    public ProblemDetail handleBaremeOperationnelNotationException(
            BaremeOperationnelNotationException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(ClientCentifParticulierException.class)
    public ProblemDetail handleClientCentifParticulierException(ClientCentifParticulierException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(ClientParticulierCentifException.class)
    public ProblemDetail handleClientParticulierCentifException(ClientParticulierCentifException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(UtilisateurException.class)
    public ProblemDetail handleUtilisateurException(UtilisateurException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(TokenMotPasseOublieException.class)
    public ProblemDetail handleTokenMotPasseOublieException(TokenMotPasseOublieException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(ProfilLabClientException.class)
    public ProblemDetail handleProfilLabClientException(ProfilLabClientException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(AbonnementEntrepriseException.class)
    public ProblemDetail handleAbonnementEntreprise(AbonnementEntrepriseException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ProblemDetail handleAuthenticationException(AuthenticationException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(ConformiteMotPasseException.class)
    public ProblemDetail handleConformiteMotPasseException(ConformiteMotPasseException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDeniedException() {
        String message = "Vous n'êtes pas autorisé à accéder à cette ressource !";
        return ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, message);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ProblemDetail handleExpiredJwtException() {
        String message = "Le token a expiré !";
        return ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, message);
    }

    @ExceptionHandler(SignatureException.class)
    public ProblemDetail handleSignatureException() {
        String message = "Le token est invalide !";
        return ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, message);
    }

    @ExceptionHandler(AffectationEntiteException.class)
    public ProblemDetail handleAffectationEntiteException(AffectationEntiteException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(DeviseException.class)
    public ProblemDetail handleDeviseException(DeviseException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(ProfilEntrepriseDeclarationException.class)
    public ProblemDetail handleProfilEntrepriseDeclarationException(ProfilEntrepriseDeclarationException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
    }
    */
}
