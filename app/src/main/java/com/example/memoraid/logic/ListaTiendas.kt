package com.example.memoraid.logic

import com.example.memoraid.data.entities.DatosTienda

class ListaTiendas {
    fun getData(): List<DatosTienda> {
        val data = arrayListOf<DatosTienda>(
            DatosTienda(
                1,
                "San Nicolás",
                "Comida",
                "Centro Comercial El Condado, Piso: 4, Local: 427 B, Av de la Prensa S/N, Quito 170301, Ecuador, Provincia de Pichincha.",
                "9:00 AM",
                "5:00 PM",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT5rwemQfIKC-xhQplcPVTj6WjgExNrxt6bng&usqp=CAU"
            ),
            DatosTienda(
                2,
                "Tienda de Ropa",
                "Ropa",
                "Dirección de la tienda de ropa",
                "10:00 AM",
                "6:00 PM",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTWG1_slPecM86VMDW8tgYS6LAnwvOKvfPjFQ&usqp=CAU"
            ),
            // Agrega más tiendas aquí con sus respectivos datos
            DatosTienda(
                3,
                "Boutique ModaFina",
                "Ropa",
                "Av. República de El Salvador N34-78 y Naciones Unidas, Quito 170135, Provincia de Pichincha, Ecuador.",
                "10:30 AM",
                "7:30 PM",
                "https://images.squarespace-cdn.com/content/v1/54747d63e4b03a6d7164fde8/1421426625817-K49I0E1PIYVVT4P9B0GK/Isla+Bosmediano.jpg"
            ),
            DatosTienda(
                4,
                "Librería La Sabiduría",
                "Libros",
                "Calle Benalcázar N8-85 y Montúfar, Quito 170401, Provincia de Pichincha, Ecuador.",
                "8:00 AM",
                "6:00 PM",
                "https://salome.com.ec/img/cms/LABRADOR%20LOCAL_1.jpg"
            ),
            DatosTienda(
                5,
                "Cafetería DeliciaLatte",
                "Café",
                "Av. González Suárez N12-34 y Wilson, Quito 170104, Provincia de Pichincha, Ecuador.",
                "7:30 AM",
                "9:00 PM",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSsgX5rTzdTAh_rXLEvEmJHNLnEdA9TKC88WQ&usqp=CAU"
            ),
            DatosTienda(
                6,
                "Joyería BrillanteOro",
                "Joyería",
                "Centro Comercial Quicentro Sur, Local 210, Av. Morán Valverde y Av. Ajaví, Quito 170117, Provincia de Pichincha, Ecuador.",
                "10:00 AM",
                "8:00 PM",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcReF1CdTFxyQx3w6B34UxlIq-9q6zEQHMtgng&usqp=CAU"
            ),
        )
        return data
    }
}