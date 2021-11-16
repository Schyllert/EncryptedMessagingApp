resource "aws_nat_gateway" "nat-gw1" {
    
    # The ID of the EIP that is to be assigned to the gateway
    allocation_id = aws_eip.nat1.id

    # The Subnet ID of the subnet that the NAT is to be deployed in
    subnet_id = aws_subnet.server-public.id

    tags = {
        Name = "Nat 1"
    }
}