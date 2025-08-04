package applicationcode.usecase.calculateshippingrequestresponse;


public interface Provided {
    static Provided create(Required required) {
        return new UseCase(required);
    }

    CalculateShippingResponse handle(CalculateShippingRequest request);
}
