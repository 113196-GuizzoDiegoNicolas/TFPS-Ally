# Sequences Diagrams

## Ping

```plantuml
@startuml
[[!include ./diagrams/sequences.puml]]
@enduml
```
## Contact

###### Crear un tipo de contacto
```plantuml
@startuml
[[!include ./diagrams/create_contactType_sequences.puml]]
@enduml
```
###### Crear un contacto
```plantuml
@startuml
[[!include ./diagrams/create_contact_sequences.puml]]
@enduml
```
###### Modificar un contacto
```plantuml
@startuml
[[!include ./diagrams/update_contact_sequences.puml]]
@enduml
```
###### Obtener contacto por ID
```plantuml
@startuml
[[!include ./diagrams/contact_byID_sequences.puml]]
@enduml
```
###### Obtener todos los contactos
```plantuml
@startuml
[[!include ./diagrams/contact_controller_sequences.puml]]
@enduml
```

###### Eliminar contacto
```plantuml
@startuml
[[!include ./diagrams/delete_contact_sequence.puml]]
@enduml
```