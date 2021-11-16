#1. create a vpc
resource "aws_vpc" "prod-vpc" {
    cidr_block = "10.0.0.0/16"
    instance_tenancy = "default"

    # Required for EKS. Enable/disable dns support
    enable_dns_support = true

    # Required for EKS. Enable/disable DNS host
    enable_dns_hostnames = true
    
    tags = {
      "Name" = "Prod-vpc"
    }
}