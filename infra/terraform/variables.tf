variable "aws_region" {
  description = "AWS region for the Spring test app instance."
  type        = string
  default     = "us-east-1"
}

variable "project_name" {
  description = "Name prefix for AWS resources."
  type        = string
  default     = "capstone-spring"
}

variable "instance_type" {
  description = "EC2 instance type. t3.small (2 GiB) is the practical minimum for Postgres + Keycloak + Spring."
  type        = string
  default     = "t3.small"
}

variable "allowed_cidrs" {
  description = "CIDR blocks allowed to reach SSH and the app (team IPs, office VPN, scanner egress, etc.)."
  type        = list(string)

  validation {
    condition     = length(var.allowed_cidrs) > 0
    error_message = "At least one allowed CIDR is required. The instance must not be open to the world."
  }

  validation {
    condition = alltrue([
      for cidr in var.allowed_cidrs : can(cidrhost(cidr, 0))
    ])
    error_message = "Each allowed_cidrs entry must be a valid CIDR (for example 203.0.113.10/32)."
  }

  validation {
    condition = alltrue([
      for cidr in var.allowed_cidrs : cidr != "0.0.0.0/0" && cidr != "::/0"
    ])
    error_message = "0.0.0.0/0 and ::/0 are not allowed. Restrict access to team and scanner CIDRs only."
  }
}

variable "app_port" {
  description = "Host port for the Spring application."
  type        = number
  default     = 8081
}

variable "keycloak_port" {
  description = "Host port for Keycloak (browser login only)."
  type        = number
  default     = 8080
}

variable "root_volume_size_gb" {
  description = "Root EBS volume size in GiB."
  type        = number
  default     = 20
}
