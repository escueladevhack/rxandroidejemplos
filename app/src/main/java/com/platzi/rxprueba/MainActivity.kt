package com.platzi.rxprueba

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnEnviarMensaje.setOnClickListener {
            Toast.makeText(this, "Enviando Mensaje", Toast.LENGTH_SHORT).show()
        }

        bntIniProceso.setOnClickListener { view ->
            lblResultado.text = "Procesando...."

            disposable = recorrerDatos()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<Boolean>() {
                    override fun onComplete() {
                        lblResultado.text = "Termino procesamiento"
                    }

                    override fun onNext(resp: Boolean) {
                        Log.i("RxAndroid", resp.toString())
                    }

                    override fun onError(e: Throwable) {
                        Log.e("RxAndroid", e.message)
                    }

                })


        }

        btnIniProceso2.setOnClickListener { view ->
            lblResultado.text = "Procesando...."

            disposable = recorrerDatos()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { resp ->
                        lblResultado.text = "Termino procesamiento"
                        Log.i("RxAndroid", resp.toString())
                    },
                    { error ->
                        Log.i("RxAndroid", error.message)
                    }

                )

        }

        btnIniProceso3.setOnClickListener { view ->
            lblResultado.text = "Procesando...."

            disposable = recorrerDatos()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onNext = { Log.i("RxAndroid", it.toString()) },
                    onError = { it.printStackTrace() },
                    onComplete = {
                        lblResultado.text = "Termino procesamiento"
                    }
                )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }

    fun recorrerDatos(): Observable<Boolean> {

        val observable = Observable.create<Boolean> { emmiter ->
            try {
                for (num in 1..1000000) {
                    Log.i("RxAndroid", num.toString())
                }

                emmiter.onNext(true)
                emmiter.onComplete()
            } catch (e: Exception) {
                emmiter.onError(e)
            }
        }

        lblResultado.text = "Paso por el observable"
        return observable
    }


}
