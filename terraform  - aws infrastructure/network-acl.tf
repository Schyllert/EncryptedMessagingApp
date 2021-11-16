
resource "aws_network_acl" "main" {
   vpc_id = aws_vpc.prod-vpc.id

    # Traffic leaving vpc
   egress { 
     protocol = "-1"  
     rule_no = 100
     action = "allow"
     cidr_block = "0.0.0.0/0"
     /* ipv6_cidr_block = "::/0" */
     from_port = 0
     to_port = 0
   } 
    
    # Traffic entering vpc
   ingress {
     protocol = "-1"  
     rule_no = 200
     action = "allow"
     cidr_block = "0.0.0.0/0"
     /* ipv6_cidr_block = "::/0" */
     from_port = 0
     to_port = 0  
    } 
}