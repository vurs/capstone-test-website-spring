# All connection details are sensitive so `terraform apply` does not print them
# in public CI logs. Values are still readable via `terraform output -raw ...`
# and are written to private SSM by the deploy workflow.

output "instance_id" {
  description = "EC2 instance ID."
  value       = aws_instance.app.id
  sensitive   = true
}

output "public_ip" {
  description = "Elastic IP address for the instance."
  value       = aws_eip.app.public_ip
  sensitive   = true
}

output "public_dns" {
  description = "Public DNS name of the instance."
  value       = aws_instance.app.public_dns
  sensitive   = true
}

output "app_url" {
  description = "Base URL for the Spring application."
  value       = "http://${aws_eip.app.public_ip}:${var.app_port}"
  sensitive   = true
}

output "openapi_url" {
  description = "OpenAPI document URL."
  value       = "http://${aws_eip.app.public_ip}:${var.app_port}/v3/api-docs"
  sensitive   = true
}

output "keycloak_url" {
  description = "Keycloak base URL."
  value       = "http://${aws_eip.app.public_ip}:${var.keycloak_port}"
  sensitive   = true
}

output "ssh_host" {
  description = "SSH target for operators and Ansible."
  value       = "ubuntu@${aws_eip.app.public_ip}"
  sensitive   = true
}

output "security_group_id" {
  description = "Security group restricting inbound access."
  value       = aws_security_group.app.id
  sensitive   = true
}

output "ssh_private_key_pem" {
  description = "Private SSH key for Ansible and operator access."
  value       = tls_private_key.ssh.private_key_openssh
  sensitive   = true
}
