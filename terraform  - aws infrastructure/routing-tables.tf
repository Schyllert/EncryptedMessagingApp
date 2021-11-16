
# Create a Route Table for the public server subnet
resource "aws_route_table" "public" {
  vpc_id = aws_vpc.prod-vpc.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.gw.id
  }

  route {
    ipv6_cidr_block        = "::/0"
    gateway_id = aws_internet_gateway.gw.id
  }

  tags = {
    Name = "Prod-public"
  }
}

# Create a Route Table for the private database subnet
resource "aws_route_table" "private" {
  vpc_id = aws_vpc.prod-vpc.id

  route {
    cidr_block = "0.0.0.0/0"
    nat_gateway_id = aws_nat_gateway.nat-gw1.id
  }

/*   route {
    ipv6_cidr_block        = "::/0"
    gateway_id = aws_nat_gateway.nat-gw1.id
  } */

  tags = {
    Name = "Prod-private"
  }
}