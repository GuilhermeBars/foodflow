package br.edu.puccampinas.foodflow.payment.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

interface SpringDataPaymentRecordRepository extends JpaRepository<PaymentRecordJpaEntity, String> {
}
