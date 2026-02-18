package autolife.dto.dados;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Token de autenticação")
public record DadosTokenJWT(
        @Schema(example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJBUEkgQ29uc3VsdGEgLSBJTkYwMTIiLCJzdWIiOiJhZG1pbiIsImV4cCI6MTc2OTk5MTI2MH0.DCoHIHHhM-Su71y5QWGVoRszR_gDxOY0uS-04VDYc4M")
        String token) {
}