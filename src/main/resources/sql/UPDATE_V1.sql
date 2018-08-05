ALTER TABLE `measurement_sheet` ADD `TEMPLATE_VERSION` INT NOT NULL AFTER `log_user` 

--
-- Table structure for table `managed_document`
--


CREATE TABLE `managed_document` (
  `id` bigint(20) NOT NULL,
  `description` varchar(500) DEFAULT NULL,
  `file_size` bigint(20) NOT NULL,
  `filename` varchar(50) DEFAULT NULL,
  `url` varchar(200) NOT NULL,
  `version` int(11) DEFAULT NULL,
  `aggreement` bigint(20) DEFAULT NULL,
  `log_user` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `managed_document`
--
ALTER TABLE `managed_document`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK5715BB652DCF72` (`aggreement`),
  ADD KEY `FK5715BBF6EC5FA2` (`log_user`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `managed_document`
--
ALTER TABLE `managed_document`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=28;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `managed_document`
--
ALTER TABLE `managed_document`
  ADD CONSTRAINT `FK5715BB652DCF72` FOREIGN KEY (`aggreement`) REFERENCES `aggreement` (`id`),
  ADD CONSTRAINT `FK5715BBF6EC5FA2` FOREIGN KEY (`log_user`) REFERENCES `log_user` (`id`);
COMMIT;