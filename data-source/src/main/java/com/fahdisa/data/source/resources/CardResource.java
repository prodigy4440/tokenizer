package com.fahdisa.data.source.resources;


import com.fahdisa.data.common.api.CardInfo;
import com.fahdisa.data.source.api.ApiResponse;
import com.fahdisa.data.source.core.CardService;
import io.swagger.annotations.Api;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Api(
        tags = {"Card Resource"}
)
@Path("/v1/card")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CardResource {

    private CardService cardService;

    public CardResource(CardService cardService) {
        this.cardService = cardService;
    }

    @POST
    public Response submit(@Valid CardInfo cardInfo) {
        ApiResponse apiResponse = cardService.submit(cardInfo);
        if (!apiResponse.getSuccess()) {
            return Response.status(Response.Status.BAD_GATEWAY)
                    .entity(apiResponse)
                    .build();
        }
        return Response.ok(apiResponse).build();
    }
}
