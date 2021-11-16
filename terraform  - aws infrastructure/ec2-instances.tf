

resource "aws_instance" "server-instance" {
  ami           = "ami-0747bdcabd34c712a" # us-east-1
  instance_type = "t2.micro"
  subnet_id = aws_subnet.server-public.id
  private_ip = "10.0.1.50"
  vpc_security_group_ids = [aws_security_group.allow_web.id]

  # Try to always add availability zone to avoid weird problems
  availability_zone = "us-east-1a"
  key_name = "eric-terraform"
  associate_public_ip_address = true

    user_data = <<-EOF
        #!/bin/bash
        sudo apt update -y
        sudo apt install apache2 -y
        sudo systemctl start apache2
        sudo bash -c 'echo your very first server > /var/www/html/index.html'
        EOF

tags = {
    Name = "prod-server"
}

}


resource "aws_instance" "database-instance" {
  ami           = "ami-0747bdcabd34c712a" # us-east-1
  instance_type = "t2.micro"
  subnet_id = aws_subnet.database-private.id
  private_ip = "10.0.2.50"
  vpc_security_group_ids = [aws_security_group.allow_web.id]

  # Try to always add availability zone to avoid weird problems
  availability_zone = "us-east-1a"
  key_name = "eric-terraform"
  associate_public_ip_address = false

      user_data = <<-EOF
        #!/bin/bash
        sudo apt update -y
        sudo apt install apache2 -y
        sudo systemctl start apache2
        sudo bash -c 'This is webpage of our database > /var/www/html/index.html'
        EOF

tags = {
    Name = "prod-database"
}

}