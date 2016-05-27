package scalaE.monocle

import monocle.Lens

/**
  * Lenses are a way to "update" immutable objects.
  */
object LensE {
  case class Employee(name: String, address: Address, company: Company)
  case class Address(street: String, number: Int)
  case class Company(company: String, address: Address)


  val employee = Employee(
    "emiliano",
    Address("scala street", 4),
    Company("google", Address("somewhere in ireland", 100))
  )

  // say you want to update the address of the company the employee works for,
  // one needs to go deep down to the nested level to do it.
  val newEmployee = employee.copy(company = employee.company.copy(address = Address("san francisco", 10)))

  // with lenses:

  val companyLens = Lens[Employee, Company](_.company)(c => e => e.copy(company = c))
  val companyAddressLens = Lens[Company, Address](_.address)(a => c => c.copy(address = a))
  val addressAdressLens = Lens[Address, String](_.street)(s => a => a.copy(street = s))

  // modify the address object in the company object
  val e1 = companyLens.composeLens(companyAddressLens).modify(address => Address("123", 12))(employee)
  // Employee(emiliano,Address(scala street,4), Company(google, Address(123,12)))

  // modify the address field in the address object in the company
  val e2 = companyLens.composeLens(companyAddressLens).composeLens(addressAdressLens).modify(street => street + " 123")(employee)
  // Employee(emiliano,Address(scala street,4),Company(google,Address(somewhere in ireland 123,100)))


}
