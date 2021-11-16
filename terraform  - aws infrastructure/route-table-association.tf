 # Associate public subnet with public Route Table
resource "aws_route_table_association" "public" {
    subnet_id      = aws_subnet.server-public.id
    route_table_id = aws_route_table.public.id
}

 # Associate private subnet with private Route Table
resource "aws_route_table_association" "private" {
    subnet_id      = aws_subnet.database-private.id
    route_table_id = aws_route_table.private.id
}