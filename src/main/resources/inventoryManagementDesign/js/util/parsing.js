export class Parsing{

    static parseDouble(stringDouble){
        var response = stringDouble;
        response = stringDouble.replace(/,/g, '');
        response = parseFloat(response)
        return response;
    }

}