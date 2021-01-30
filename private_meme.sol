pragma solidity >=0.7.0 <0.8.0;
contract mem{
    string url;
    address owner;
    constructor(string memory url_mem){
       url = url_mem;
       owner = msg.sender;
    }
    function return_mem() public payable returns(string memory){
        return url;
    }
}
contract pack{
    address [] urls;
    uint128 len = 0;
    constructor(address url_mem){
       urls[len] = url_mem;
       len += 1;
    }
    function return_mem() public payable returns(address[] memory ){
        return urls;
    }
}