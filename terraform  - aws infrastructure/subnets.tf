

#4a. Create a subnet for server subnet
resource "aws_subnet" "server-public" {
  vpc_id  = aws_vpc.prod-vpc.id
  cidr_block = "10.0.1.0/24"
  availability_zone = "us-east-1a"

  # Required for EKS
  map_public_ip_on_launch = true


  tags = {
    Name = "prod-subnet-server"
    "kubernetes.io/cluster/eks" = "shared"
    "kubernetes.io/role/elb" = 1
  }
}

#4b. Create a subnet for database subnet
resource "aws_subnet" "database-private" {
  vpc_id  = aws_vpc.prod-vpc.id
  cidr_block = "10.0.2.0/24"
  availability_zone = "us-east-1a"
  map_public_ip_on_launch = false


  tags = {
    Name = "prod-subnet-database"
    "kubernetes.io/cluster/eks" = "shared"
    "kubernetes.io/role/elb" = 1
  }
}