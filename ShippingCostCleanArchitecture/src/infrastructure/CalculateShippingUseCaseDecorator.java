package infrastructure;

import applicationcode.usecase.calculateshippingrequestresponse.CalculateShippingRequest;
import applicationcode.usecase.calculateshippingrequestresponse.CalculateShippingResponse;

public class CalculateShippingUseCaseDecorator implements applicationcode.usecase.calculateshippingrequestresponse.Provided {

    private double totalCost;
    private final applicationcode.usecase.calculateshippingrequestresponse.Provided decoratee;

    public CalculateShippingUseCaseDecorator(applicationcode.usecase.calculateshippingrequestresponse.Provided decoratee) {
        this.decoratee = decoratee;
    }


    @Override
    public CalculateShippingResponse handle(CalculateShippingRequest request) {
        CalculateShippingResponse response = decoratee.handle(request);
        totalCost+= response.getCost();
        return response;
    }
}
