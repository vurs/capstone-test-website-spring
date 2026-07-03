output "instance_id" {
  description = "EC2 instance ID."
  value       = aws_instance.app.id
}

output "public_ip" {
  description = "Elastic IP address for the instance."
  value       = aws_eip.app.public_ip
}

output "public_dns" {
  description = "Public DNS name of the instance."
  value       = aws_instance.app.public_dns
}

output "app_url" {
  description = "Base URL for the Spring application."
  value       = "http://${aws_eip.app.public_ip}:${var.app_port}"
}

output "openapi_url" {
  description = "OpenAPI document URL."
  value       = "http://${aws_eip.app.public_ip}:${var.app_port}/v3/api-docs"
}

output "keycloak_url" {
  description = "Keycloak base URL."
  value       = "http://${aws_eip.app.public_ip}:${var.keycloak_port}"
}

output "ssh_host" {
  description = "SSH target for operators and Ansible."
  value       = "ubuntu@${aws_eip.app.public_ip}"
}

output "security_group_id" {
  description = "Security group restricting inbound access."
  value       = aws_security_group.app.id
}

output "allowed_cidrs" {
  description = "CIDRs currently allowed inbound."
  value       = var.allowed_cidrs
}

output "ssh_private_key_pem" {
  description = "Private SSH key for Ansible and operator access."
  value       = tls_private_key.ssh.private_key_openssh
  sensitive   = true
}
