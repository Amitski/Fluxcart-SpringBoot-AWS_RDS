# Fluxcart-SpringBoot-AWS_RDS

**Identity Reconciliation API**

This API is designed to help FluxKart.com link different orders made with different contact information to the same person for a personalized customer experience.

## Table of Contents
- [Problem Statement](#problem-statement)
- [Requirements](#requirements)
- [How to Use](#how-to-use)
- [Contributing](#contributing)
- [Tech Stack](#tech-stack)

## Problem Statement

Meet Dr. Emmett Brown, stuck in 2023, who is fixing his time machine and shopping for parts on FluxKart.com. To avoid detection, he uses different email addresses and phone numbers for each purchase. FluxKart wants to provide a personalized experience and needs a way to identify customers across multiple purchases, even if they use different contact information.

## Requirements

- Design a web service with an endpoint `/identify` that handles HTTP POST requests.
- The API should receive JSON data with either an email or a phone number.
- It should return consolidated contact information for the same customer.
- If no existing contact is found, it should create a new primary contact.
- If new information is provided, it should create secondary contacts.
- Primary contacts can turn into secondary contacts.

## How to Use

To use this API, make HTTP POST requests to the 'http://bitespeed.ap-south-1.elasticbeanstalk.com/identify' endpoint with JSON data containing either an email and/or a phone number. The API will respond with consolidated contact information.

Example Request:

```json
{
  "email": "mcfly@hillvalley.edu",
  "phoneNumber": "123456"
}

Example Response:

```json
{
  "contact": {
    "primaryContatctId": 1,
    "emails": ["lorraine@hillvalley.edu", "mcfly@hillvalley.edu"],
    "phoneNumbers": ["123456"],
    "secondaryContactIds": [23]
  }
}

Tech Stack
Database: AWS RDS-DB
Backend Framework: SpringBoot

Feel free to reach out if you have any questions or need assistance.

Happy coding!
